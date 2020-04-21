package com.securenative;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.actions.ActionManager;
import com.securenative.actions.ApiRoute;
import com.securenative.configurations.AgentConfigOptions;
import com.securenative.configurations.ExecuteManager;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.events.Event;
import com.securenative.events.EventFactory;
import com.securenative.events.EventManager;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.interceptors.InterceptorManager;
import com.securenative.models.AgentLoginResponse;
import com.securenative.models.EventTypes;
import com.securenative.models.RiskResult;
import com.securenative.module.ModuleManager;
import com.securenative.rules.RuleManager;
import com.securenative.utils.Logger;
import com.securenative.utils.Utils;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.json.JSONObject;

import java.time.Duration;

public class SecureNative {
    private final String API_URL = "https://api.securenative.com/collector/api/v1";
    private final int INTERVAL = 1000;
    private final int MAX_EVENTS = 1000;
    private final Boolean AUTO_SEND = true;
    private final Boolean SDK_ENABLED = true;
    private final Boolean DEBUG_LOG = false;
    private final int DEFAULT_TIMEOUT = 1500;
    private String configUpdateTimestamp = Utils.generateTimestamp();

    private Boolean isAgentStarted = false;
    private EventManager eventManager;
    private SecureNativeOptions snOptions;
    private String apiKey;
    private ObjectMapper mapper;
    private RuleManager ruleManager;
    private ExecuteManager heartbeat;
    private ExecuteManager configurationUpdater;
    private ModuleManager moduleManager;

    public SecureNative(ModuleManager moduleManager, SecureNativeOptions snOptions) throws SecureNativeSDKException {
        this.apiKey = snOptions.getApiKey();
        this.snOptions = initializeOptions(snOptions);
        Logger.setLoggingEnable(this.snOptions.getDebugMode());
        this.eventManager = new EventManager(this.apiKey, this.snOptions);
        this.moduleManager = moduleManager;
        this.snOptions = snOptions;
        this.ruleManager = new RuleManager();
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!this.snOptions.isAgentDisable()) {
            // apply interceptors
            InterceptorManager.applyModuleInterceptors(this);
        }
    }

    private SecureNativeOptions initializeOptions(SecureNativeOptions options) {
        if (options == null) {
            Logger.getLogger().info("SecureNative options are empty, initializing default values");
            options = new SecureNativeOptions();
        }
        if (Utils.isNullOrEmpty(options.getApiUrl())) {
            options.setApiUrl(API_URL);
        }

        if (options.getInterval() == 0) {
            options.setInterval(INTERVAL);
        }

        if (options.getAgentDisable() == null) {
            options.setAgentDisable(false);
        }

        if (options.getMaxEvents() == 0) {
            options.setMaxEvents(MAX_EVENTS);
        }
        if (options.isAutoSend() == null) {
            options.setAutoSend(AUTO_SEND);
        }
        if (options.getSdkEnabled() == null) {
            options.setSdkEnabled(SDK_ENABLED);
        }
        if (options.getDebugMode() == null) {
            options.setSdkEnabled(DEBUG_LOG);
        }
        if (options.getTimeout() == 0) {
            options.setTimeout(DEFAULT_TIMEOUT);
        }
        if (options.getDebugMode() == null) {
            options.setDebugMode(false);
        }

        return options;
    }

    private void handleConfigUpdate(AgentConfigOptions config) {
        Logger.getLogger().debug("Handling configuration update");

        if (config == null) {
            return;
        }

        if (config.getTimestamp().compareTo(this.configUpdateTimestamp) > 0) {
            this.configUpdateTimestamp = config.getTimestamp();
        }

        // enforce all rules
        if (config.getRules() != null) {
            this.ruleManager.enforceRules(config.getRules());
        }

        // enforce all actions
        if (config.getActions() != null) {
            ActionManager.enforceActions(config.getActions());
        }
    }

    public void error(Error err) {
        Logger.getLogger().debug("Error", err);
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.ERROR.getRoute());
        Event event = EventFactory.createEvent(EventTypes.ERROR, err.toString());
        this.eventManager.sendAsync(event, requestUrl);
    }

    public String agentLogin() throws Exception {
        Logger.getLogger().debug("Performing agent login");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.LOGIN.getRoute());

        String framework = this.moduleManager.getFramework();
        String frameworkVersion = this.moduleManager.getFrameworkVersion();

        Event loginEvent = EventFactory.createEvent(EventTypes.AGENT_LOG_IN, framework, frameworkVersion, this.snOptions.getAppName());
        String r = this.eventManager.sendAgentEvent(loginEvent, requestUrl);
        AgentLoginResponse res = mapper.readValue(r, AgentLoginResponse.class);

        // Update config
        this.handleConfigUpdate(res.getConfig());

        // Start heartbeat manager
        Logger.getLogger().debug("Starting heartbeat manager");
        this.heartbeat = new ExecuteManager(this.snOptions.getHeartbeatDelay(), this.snOptions.getHeartbeatPeriod(), this.heartbeatTask());
        heartbeat.execute();

        // Start configuration updater
        Logger.getLogger().debug("Starting configuration update manager");
        this.configurationUpdater = new ExecuteManager(this.snOptions.getConfigUpdateDelay(), this.snOptions.getConfigUpdatePeriod(), this.configUpdaterTask());
        configurationUpdater.execute();

        if (res.getSessionId().toLowerCase().equals("invalid api key id")) {
            Logger.getLogger().debug("Failed to perform agent login: Invalid api key id");
            return null;
        }

        Logger.getLogger().debug(String.format("Agent successfully logged-in, sessionId: %s", res.getSessionId()));
        return res.getSessionId();
    }

    public Boolean agentLogout() {
        Logger.getLogger().debug("Performing agent logout");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.LOGOUT.getRoute());

        Event event = EventFactory.createEvent(EventTypes.AGENT_LOG_OUT);
        try {
            this.eventManager.sendAgentEvent(event, requestUrl);
            this.heartbeat.shutdown();
            this.configurationUpdater.shutdown();
            Logger.getLogger().debug("Agent successfully logged-out");
            return true;
        } catch (Exception e) {
            Logger.getLogger().debug(String.join("Failed to perform agent logout; ", e.toString()));
        }
        return false;
    }

    public void startAgent() {
        if (!this.isAgentStarted) {
            Logger.getLogger().debug("Attempting to start agent");
            if (this.snOptions.getApiKey() == null) {
                Logger.getLogger().error("You must pass your SecureNative api key");
                return;
            }

            if (this.snOptions.isAgentDisable()) {
                Logger.getLogger().debug("Skipping agent start");
                return;
            }

            // obtain session
            RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                    .handle(Exception.class)
                    .withDelay(Duration.ofSeconds((long) (Math.ceil(Math.random() * 10) + 1) * 10))
                    .withMaxRetries(3);
            String sessionId = Failsafe.with(retryPolicy).get(this::agentLogin);

            if (sessionId != null) {
                sessionId = parseSessionId(sessionId);
                InterceptorManager.applyAgentInterceptor(sessionId, this.moduleManager.getFramework());
                this.isAgentStarted = true;
                Logger.getLogger().debug("Agent successfully started!");
            } else {
                Logger.getLogger().debug("No session obtained, unable to start agent!");
                this.isAgentStarted = false;
            }
        } else {
            Logger.getLogger().debug("Agent already started, skipping");
        }
    }

    public void stopAgent() {
        if (this.isAgentStarted) {
            Logger.getLogger().debug("Attempting to stop agent");
            this.eventManager.flush();
            Boolean status = this.agentLogout();
            if (status) {
                this.isAgentStarted = false;
            }
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    private String parseSessionId(String sessionId) {
        final JSONObject session = new JSONObject(sessionId);
        return session.getString("sessionId");
    }

    public SecureNativeOptions getSecureNativeOptions() {
        return this.snOptions;
    }

    private Runnable heartbeatTask() {
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.HEARTBEAT.getRoute());
        Event event = EventFactory.createEvent(EventTypes.HEARTBEAT);
        Logger.getLogger().debug("Sending heartbeat event");

        return () -> this.eventManager.sendAsync(event, requestUrl);
    }

    private Runnable configUpdaterTask() {
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.CONFIG.getRoute());
        Event event = EventFactory.createEvent(EventTypes.CONFIG, this.snOptions.getHostId(), this.snOptions.getAppName());
        Logger.getLogger().debug("Sending configuration update event");

        AgentConfigOptions config = this.eventManager.sendUpdateConfigEvent(event, requestUrl);
        return () -> this.handleConfigUpdate(config);
    }

    // SDK event
    public void track(Event event) {
        Logger.getLogger().info("Track event call");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.TRACK.getRoute());
        this.eventManager.sendAsync(event, requestUrl);
    }

    // SDK event
    public RiskResult verify(Event event) {
        Logger.getLogger().info("Verify event call");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.VERIFY.getRoute());
        return this.eventManager.sendSync(event, requestUrl);
    }

    // SDK event
    public RiskResult flow(long flowId, Event event) {
        Logger.getLogger().info("Flow event call");
        String requestUrl = String.format("%s/%s/%s", this.snOptions.getApiUrl(), ApiRoute.FLOW.getRoute(), flowId);
        return this.eventManager.sendSync(event, requestUrl);
    }

    // SDK event
    public RiskResult risk(Event event) {
        Logger.getLogger().info("Risk call");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.RISK.getRoute());
        return this.eventManager.sendSync(event, requestUrl);
    }
}