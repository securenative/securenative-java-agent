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

import java.time.Duration;

public class SecureNative {
    private String configUpdateTimestamp = Utils.generateTimestamp();
    private Boolean isAgentStarted = false;
    private final EventManager eventManager;
    private SecureNativeOptions snOptions;
    private final String apiKey;
    private final ObjectMapper mapper;
    private final RuleManager ruleManager;
    private ExecuteManager heartbeat;
    private ExecuteManager configurationUpdater;
    private final ModuleManager moduleManager;

    public static final Logger logger = Logger.getLogger(SecureNative.class);

    public SecureNative(ModuleManager moduleManager, SecureNativeOptions snOptions) throws SecureNativeSDKException {
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.snOptions = snOptions;
        this.apiKey = snOptions.getApiKey();
        this.eventManager = new EventManager(this.apiKey, this.snOptions);
        this.moduleManager = moduleManager;
        this.snOptions = snOptions;
        this.ruleManager = new RuleManager();

        if (!this.snOptions.isAgentDisable()) {
            // apply interceptors
            InterceptorManager.applyModuleInterceptors(this);
        }
    }

    private void handleConfigUpdate(AgentConfigOptions config) {
        logger.debug("Handling configuration update");

        if (config == null) {
            return;
        }

        if (config.getTimestamp().compareTo(this.configUpdateTimestamp) > 0) {
            this.configUpdateTimestamp = config.getTimestamp();
        }

        // enforce all rules
        if (config.getRules() != null && config.getRules().size() > 0) {
            this.ruleManager.enforceRules(config.getRules());
        }

        // enforce all actions
        if (config.getActions() != null && config.getActions().size() > 0) {
            ActionManager.enforceActions(config.getActions());
        }
    }

    public void error(Error err) {
        logger.debug("Error", err);
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.ERROR.getRoute());
        Event event = EventFactory.createEvent(EventTypes.ERROR, err.toString());
        this.eventManager.sendAsync(event, requestUrl);
    }

    public String agentLogin() throws Exception {
        logger.debug("Performing agent login");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.LOGIN.getRoute());

        String framework = this.moduleManager.getFramework();
        String frameworkVersion = this.moduleManager.getFrameworkVersion();

        Event loginEvent = EventFactory.createEvent(EventTypes.AGENT_LOG_IN, framework, frameworkVersion, this.snOptions.getAppName());
        String r = this.eventManager.sendAgentEvent(loginEvent, requestUrl);
        AgentLoginResponse res = mapper.readValue(r, AgentLoginResponse.class);

        // Update config
        this.handleConfigUpdate(res.getConfig());

        if (res.getSessionId().toLowerCase().equals("invalid api key id")) {
            logger.debug("Failed to perform agent login: Invalid api key id");
            return null;
        }

        logger.debug(String.format("Agent successfully logged-in, sessionId: %s", res.getSessionId()));
        return res.getSessionId();
    }

    public Boolean agentLogout() {
        logger.debug("Performing agent logout");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.LOGOUT.getRoute());

        Event event = EventFactory.createEvent(EventTypes.AGENT_LOG_OUT);
        try {
            this.eventManager.sendAgentEvent(event, requestUrl);
            this.heartbeat.shutdown();
            this.configurationUpdater.shutdown();
            logger.debug("Agent successfully logged-out");
            return true;
        } catch (Exception e) {
            logger.debug(String.join("Failed to perform agent logout; ", e.toString()));
        }
        return false;
    }

    public void startAgent() {
        if (!this.isAgentStarted) {
            logger.debug("Attempting to start agent");
            if (this.snOptions.getApiKey() == null) {
                logger.error("You must pass your SecureNative api key");
                return;
            }

            if (this.snOptions.isAgentDisable()) {
                logger.debug("Skipping agent start");
                return;
            }

            // obtain session
            RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                    .handle(Exception.class)
                    .withDelay(Duration.ofSeconds((long) (Math.ceil(Math.random() * 10) + 1) * 10))
                    .withMaxRetries(3);
            String sessionId = Failsafe.with(retryPolicy).get(this::agentLogin);

            if (sessionId != null) {
                InterceptorManager.applyAgentInterceptor(sessionId, this.moduleManager.getFramework());
                this.isAgentStarted = true;

                // Start heartbeat manager
                logger.debug("Starting heartbeat manager");
                this.heartbeat = new ExecuteManager(this.snOptions.getHeartbeatDelay(), this.snOptions.getHeartbeatPeriod(), "heartbeat event", this.heartbeatTask());
                heartbeat.execute();

                // Start configuration updater
                logger.debug("Starting configuration update manager");
                this.configurationUpdater = new ExecuteManager(this.snOptions.getConfigUpdateDelay(), this.snOptions.getConfigUpdatePeriod(), "configuration update event", this.configUpdaterTask());
                configurationUpdater.execute();

                logger.debug("Agent successfully started!");
            } else {
                logger.debug("No session obtained, unable to start agent!");
                this.isAgentStarted = false;
            }
        } else {
            logger.debug("Agent already started, skipping");
        }
    }

    public void stopAgent() {
        if (this.isAgentStarted) {
            logger.debug("Attempting to stop agent");
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

    public SecureNativeOptions getSecureNativeOptions() {
        return this.snOptions;
    }

    private Runnable heartbeatTask() {
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.HEARTBEAT.getRoute());
        Event event = EventFactory.createEvent(EventTypes.HEARTBEAT);
        return () -> this.eventManager.sendAsync(event, requestUrl);
    }

    private Runnable configUpdaterTask() {
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.CONFIG.getRoute());
        Event event = EventFactory.createEvent(EventTypes.CONFIG, this.snOptions.getHostId(), this.snOptions.getAppName());
        AgentConfigOptions config = this.eventManager.sendUpdateConfigEvent(event, requestUrl);
        return () -> this.handleConfigUpdate(config);
    }

    public RiskResult risk(Event event) {
        logger.debug("Risk call");
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.RISK.getRoute());
        return this.eventManager.sendSync(event, requestUrl);
    }
}