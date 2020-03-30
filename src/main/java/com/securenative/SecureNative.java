package com.securenative;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.actions.ActionManager;
import com.securenative.actions.ApiRoute;
import com.securenative.configurations.AgentConfigOptions;
import com.securenative.configurations.ConfigurationUpdaterRunnable;
import com.securenative.configurations.HeartBeatRunnable;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.events.Event;
import com.securenative.events.EventFactory;
import com.securenative.events.SnEventManager;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.interceptors.InterceptorManager;
import com.securenative.models.*;
import com.securenative.module.ModuleManager;
import com.securenative.rules.RuleManager;
import com.securenative.utils.Utils;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SecureNative {
    private final String API_URL = "https://api.securenative.com/collector/api/v1";
    private final int INTERVAL = 1000;
    private final int MAX_EVENTS = 1000;
    private final Boolean AUTO_SEND = true;
    private final Boolean SDK_ENABLED = true;
    private final Boolean DEBUG_LOG = false;
    private final int DEFAULT_TIMEOUT = 1500;

    private Boolean isAgentStarted = false;
    private SnEventManager eventManager;
    private SecureNativeOptions snOptions;
    private String apiKey;
    private Utils utils;
    private ObjectMapper mapper;

    private long configUpdateTs = 0;
    private ConfigurationUpdaterRunnable configurationUpdater;
    private HeartBeatRunnable heartBeatManager;
    public ModuleManager moduleManager;

    public SecureNative(ModuleManager moduleManager, SecureNativeOptions snOptions) throws SecureNativeSDKException {
        this.apiKey = snOptions.getApiKey();
        this.utils = new Utils();
        this.snOptions = initializeOptions(snOptions);
        Logger.setLoggingEnable(this.snOptions.getDebugMode());
        this.eventManager = new SnEventManager(this.apiKey, this.snOptions);
        this.moduleManager = moduleManager;
        this.snOptions = snOptions;
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!this.snOptions.isAgentDisable()) {
            // apply interceptors
            InterceptorManager.applyModuleInterceptors(this.moduleManager, this);
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
        Logger.getLogger().debug("Handling config update");

        if (config == null) {
            return;
        }

        if (config.getTs() > this.configUpdateTs) {
            this.configUpdateTs = config.getTs();
        }

        // enforce all rules
        if (config.getRules() != null) {
            RuleManager.enforceRules(config.getRules());
        }

        // enforce all actions
        if (config.getActions() != null) {
            ActionManager.enforceActions(config.getActions());
        }
    }

    public void error(Error err) {
        Logger.getLogger().debug("Error", err);
        String requestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.ERROR);
        Event event = EventFactory.createEvent(EventTypes.ERROR, err.toString());
        this.eventManager.sendAsync(event, requestUrl);
    }

    public String getDefaultCookieName() {
        return this.utils.COOKIE_NAME;
    }

    public String agentLogin() {
        Logger.getLogger().debug("Performing agent login");
        String loginRequestUrl = this.snOptions.getApiUrl() + "/agent-login";

        String framework = this.moduleManager.getFramework();
        String frameworkVersion = this.moduleManager.getFrameworkVersion();

        Event loginEvent = EventFactory.createEvent(EventTypes.AGENT_LOG_IN, framework, frameworkVersion, this.snOptions.getAppName());
        try {
            String r = this.eventManager.sendAgentEvent(loginEvent, loginRequestUrl);
            AgentLoginResponse res = mapper.readValue(r, AgentLoginResponse.class);

            // Update config
            this.handleConfigUpdate(res.config);

            // Start heartbeat manager
            String heartbeatRequestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.HEARTBEAT);
            Event heartbeatEvent = EventFactory.createEvent(EventTypes.ERROR, this.snOptions.getAppName());
            heartBeatManager = new HeartBeatRunnable(this.eventManager, heartbeatRequestUrl, heartbeatEvent, this.snOptions.getHeartBeatInterval());
            heartBeatManager.run();

            // Start configuration updater
            String confRequestUrl = String.format("%s/%s", this.snOptions.getApiUrl(), ApiRoute.CONFIG);
            Event confEvent = EventFactory.createEvent(EventTypes.CONFIG, this.snOptions.getHostId(), this.snOptions.getAppName());
            configurationUpdater = new ConfigurationUpdaterRunnable(this.eventManager, confRequestUrl, confEvent, this.configUpdateTs);
            configurationUpdater.run();

            if (res.getSessionId().toLowerCase().equals("invalid api key id")) {
                Logger.getLogger().debug("Failed to perform agent login: Invalid api key id");
                return null;
            }

            Logger.getLogger().debug(String.format("Agent successfully logged-in, sessionId: %s", res.getSessionId()));
            return res.getSessionId();
        } catch (Exception e) {
            Logger.getLogger().debug(String.format("Failed to perform agent login: %s", e.toString()));
        }
        return null;
    }

    public Boolean agentLogout() {
        Logger.getLogger().debug("Performing agent logout");
        String requestUrl = this.snOptions.getApiUrl() + "/agent-logout";

        Event event = EventFactory.createEvent(EventTypes.AGENT_LOG_OUT);
        try {
            this.eventManager.sendAgentEvent(event, requestUrl);
            if (this.configurationUpdater.isRunning()) {
                this.configurationUpdater.interrupt();
            }
            if (this.heartBeatManager.isRunning()) {
                this.heartBeatManager.interrupt();
            }
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

            try {
                // obtain session
                String sessionId = this.agentLogin();
                if (sessionId != null) {
                    sessionId = parseSessionId(sessionId);
                    InterceptorManager.applyAgentInterceptor(sessionId);
                    this.isAgentStarted = true;

                    Logger.getLogger().debug("Agent successfully started!");
                } else {
                    Logger.getLogger().debug("No session obtained, unable to start agent!");
                }
            } catch (Exception e){
                long backoff = (long)(Math.ceil(Math.random() *10) + 1) * 1000;
                Logger.getLogger().debug(String.format("Failed to start agent, will retry after backoff %s", backoff));
                CompletableFuture.delayedExecutor(backoff, TimeUnit.MILLISECONDS).execute(this::startAgent);
            }
        } else {
            Logger.getLogger().debug("Agent already started, skipping");
        }
    }

    public void stopAgent() {
        if (this.isAgentStarted) {
            Logger.getLogger().debug("Attempting to stop agent");
            Boolean status = this.agentLogout();
            if (status) {
                this.isAgentStarted = false;
            }
        }
    }

    public void track(Event event) {
        Logger.getLogger().info("Track event call");
        this.eventManager.sendAsync(event, this.snOptions.getApiUrl() + "/track");
    }

    public RiskResult verify(Event event) {
        Logger.getLogger().info("Verify event call");
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/verify");
    }

    public RiskResult flow(long flowId, Event event) { // FOR FUTURE PURPOSES
        Logger.getLogger().info("Flow event call");
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/flow/" + flowId);
    }

    public String getApiKey() {
        return apiKey;
    }

    private String parseSessionId(String sessionId) {
        final JSONObject session = new JSONObject(sessionId);
        return session.getString("sessionId");
    }
}