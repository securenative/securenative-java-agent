package com.securenative.events;

import com.securenative.models.EventTypes;
import com.securenative.utils.Utils;

public class AgentLogoutEvent implements Event {
    private final String eventType;
    private String timestamp;

    public AgentLogoutEvent() {
        this.eventType = EventTypes.AGENT_LOG_OUT.getType();
        this.timestamp = Utils.generateTimestamp();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
