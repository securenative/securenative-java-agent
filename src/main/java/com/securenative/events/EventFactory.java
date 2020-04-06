package com.securenative.events;

import com.securenative.models.EventTypes;

public class EventFactory {
    // TODO [MATAN]: There is usage to this method with:
    // EventTypes.ERROR and EventTypes.Config
    // but there is no handling for them (will return null)
    public static Event createEvent(EventTypes eventType, String... args) {
        if (eventType == EventTypes.AGENT_LOG_IN) {
            String framework = args[0];
            String frameworkVersion = args[1];
            String appName = args[2];
            return new AgentLoginEvent(framework, frameworkVersion, appName);
        } else if (eventType == EventTypes.AGENT_LOG_OUT) {
            return new AgentLogoutEvent();
        }
        return null;
    }
}
