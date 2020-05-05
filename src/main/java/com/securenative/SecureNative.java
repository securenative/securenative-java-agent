package com.securenative;

import com.securenative.actions.ActionManager;
import com.securenative.enums.ApiRoute;
import com.securenative.config.AgentConfigOptions;
import com.securenative.config.ExecuteManager;
import com.securenative.config.SecureNativeOptions;
import com.securenative.context.SecureNativeContextBuilder;
import com.securenative.enums.EventTypes;
import com.securenative.events.Event;
import com.securenative.events.EventFactory;
import com.securenative.events.manager.SecureNativeEventManager;
import com.securenative.exceptions.SecureNativeParseException;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.http.SecureNativeHTTPClient;
import com.securenative.middlewares.InterceptorManager;
import com.securenative.models.AgentLoginResponse;
import com.securenative.models.VerifyResult;
import com.securenative.module.ModuleManager;
import com.securenative.rules.RuleManager;
import com.securenative.utils.DateUtils;
import com.securenative.utils.Logger;
import com.securenative.utils.Utils;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import java.io.IOException;
import java.time.Duration;

public class SecureNative {
    private String configUpdateTimestamp = DateUtils.generateTimestamp();
    private Boolean isAgentStarted = false;
    private final SecureNativeEventManager eventManager;
    private SecureNativeOptions options;
    private final String apiKey;
    private final RuleManager ruleManager;
    private ExecuteManager heartbeat;
    private ExecuteManager configurationUpdater;
    private final ModuleManager moduleManager;

    public static final Logger logger = Logger.getLogger(SecureNative.class);

    public SecureNative(ModuleManager moduleManager, SecureNativeOptions options) throws SecureNativeSDKException {
        if (Utils.isNullOrEmpty(options.getApiKey())) {
            throw new SecureNativeSDKException("You must pass your SecureNative api key");
        }

        this.options = options;
        this.apiKey = options.getApiKey();
        this.eventManager = new SecureNativeEventManager(new SecureNativeHTTPClient(options), options);
        this.eventManager.startEventsPersist();
        this.moduleManager = moduleManager;
        this.options = options;
        this.ruleManager = new RuleManager();

        if (!this.options.getDisabled()) {
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

    public String agentLogin() throws Exception {
        logger.debug("Performing agent login");
        String requestUrl = String.format("%s/%s", this.options.getApiUrl(), ApiRoute.LOGIN.getRoute());

        String framework = this.moduleManager.getFramework();
        String frameworkVersion = this.moduleManager.getFrameworkVersion();

        Event loginEvent = EventFactory.createEvent(EventTypes.AGENT_LOG_IN, framework, frameworkVersion, this.options.getAppName());
        AgentLoginResponse res = this.eventManager.sendSync(AgentLoginResponse.class, loginEvent, requestUrl);

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
        String requestUrl = String.format("%s/%s", this.options.getApiUrl(), ApiRoute.LOGOUT.getRoute());

        Event event = EventFactory.createEvent(EventTypes.AGENT_LOG_OUT);
        try {
            this.eventManager.sendAsync(event, requestUrl, true);
            this.heartbeat.shutdown();
            this.configurationUpdater.shutdown();
            logger.debug("Agent successfully logged-out");
            return true;
        } catch (Exception e) {
            logger.debug(String.join("Failed to perform agent logout; ", e.toString()));
        }
        return false;
    }

    public void startAgent() throws IOException, SecureNativeParseException {
        if (!this.isAgentStarted) {
            logger.debug("Attempting to start agent");
            if (this.options.getApiKey() == null) {
                logger.error("You must pass your SecureNative api key");
                return;
            }

            if (this.options.getDisabled()) {
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
                this.heartbeat = new ExecuteManager(this.options.getHeartbeatDelay(), this.options.getHeartbeatPeriod(), "heartbeat event", this.heartbeatTask());
                heartbeat.execute();

                // Start configuration updater
                logger.debug("Starting configuration update manager");
                this.configurationUpdater = new ExecuteManager(this.options.getConfigUpdateDelay(), this.options.getConfigUpdatePeriod(), "configuration update event", this.configUpdaterTask());
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
            this.eventManager.stopEventsPersist();
            Boolean status = this.agentLogout();
            if (status) {
                this.isAgentStarted = false;
            }
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    private Runnable heartbeatTask() {
        String requestUrl = String.format("%s/%s", this.options.getApiUrl(), ApiRoute.HEARTBEAT.getRoute());
        Event event = EventFactory.createEvent(EventTypes.HEARTBEAT);
        return () -> this.eventManager.sendAsync(event, requestUrl, false);
    }

    private Runnable configUpdaterTask() throws IOException, SecureNativeParseException {
        String requestUrl = String.format("%s/%s", this.options.getApiUrl(), ApiRoute.CONFIG.getRoute());
        Event event = EventFactory.createEvent(EventTypes.CONFIG, this.options.getAppName());
        AgentConfigOptions config = this.eventManager.sendSync(AgentConfigOptions.class, event, requestUrl);
        return () -> this.handleConfigUpdate(config);
    }

    public VerifyResult risk(Event event) throws IOException, SecureNativeParseException {
        logger.debug("Risk call");
        String requestUrl = String.format("%s/%s", this.options.getApiUrl(), ApiRoute.RISK.getRoute());
        return this.eventManager.sendSync(VerifyResult.class, event, requestUrl);
    }

    public static SecureNativeContextBuilder contextBuilder() {
        return SecureNativeContextBuilder.defaultContextBuilder();
    }

    public SecureNativeOptions getOptions() {
        return this.options;
    }
}