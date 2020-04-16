package com.securenative.events;

import com.securenative.models.EventTypes;

public class EventFactory {
    public static Event createEvent(EventTypes eventType, String... args) {
        if (eventType == EventTypes.AGENT_LOG_IN) {
            String framework = args[0];
            String frameworkVersion = args[1];
            String appName = args[2];
            return new AgentLoginEvent(framework, frameworkVersion, appName);
        } else if (eventType == EventTypes.AGENT_LOG_OUT) {
            return new AgentLogoutEvent();
        } else if (eventType == EventTypes.ERROR) {
            String stackTrace = args[0];
            String message = args[1];
            return new ErrorEvent(stackTrace, message);
        } else if (eventType == EventTypes.CONFIG) {
            String hostId = args[0];
            String ts = args[1];
            String appName = args[2];
            return new ConfigEvent(hostId, appName, Long.parseLong(ts));
        } else if (eventType == EventTypes.HEARTBEAT) {
            new AgentHeartBeatEvent();
        }
        return null;
    }
}
