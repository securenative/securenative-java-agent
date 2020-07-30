package com.securenative.agent.events;

import com.securenative.agent.config.SecureNativeOptions;
import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.models.EventOptions;
import com.securenative.agent.models.RequestOptions;
import com.securenative.agent.models.User;

public class EventFactory {
    public static Event createEvent(EventTypes eventType, Object... args) {
        if (eventType == EventTypes.AGENT_LOG_IN) {
            String framework = (String) args[0];
            String frameworkVersion = (String) args[1];
            String appName = (String) args[2];
            return new AgentLoginEvent(framework, frameworkVersion, appName);
        } else if (eventType == EventTypes.AGENT_LOG_OUT) {
            return new AgentLogoutEvent();
        } else if (eventType == EventTypes.ERROR) {
            String stackTrace = (String) args[0];
            String message = (String) args[1];
            return new ErrorEvent(stackTrace, message);
        } else if (eventType == EventTypes.CONFIG) {
            String appName = (String) args[0];
            return new ConfigEvent(appName);
        } else if (eventType == EventTypes.HEARTBEAT) {
            return new AgentHeartBeatEvent();
        } else if (eventType == EventTypes.REQUEST) {
            RequestOptions options = (RequestOptions) args[0];
            return new RequestEvent(options);
        } else if (eventType == EventTypes.SDK) {
            EventOptions eventOptions = (EventOptions) args[0];
            SecureNativeOptions snOptions = (SecureNativeOptions) args[2];
            return new SDKEvent(eventOptions, snOptions);
        } else if (eventType == EventTypes.LOG_IN) {
            User user = (User) args[0];
            return new LoginEvent(user);
        } else if (eventType == EventTypes.LOG_OUT) {
            User user = (User) args[0];
            return new LogoutEvent(user);
        } else if (eventType == EventTypes.LOG_IN_CHALLENGE) {
            User user = (User) args[0];
            return new LoginChallengeEvent(user);
        } else if (eventType == EventTypes.LOG_IN_FAILURE) {
            User user = (User) args[0];
            return new LoginFailureEvent(user);
        } else if (eventType == EventTypes.PERFORMANCE) {
            return new PerformanceEvent();
        }
        return null;
    }
}
