package com.securenative.agent.events;

import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.utils.DateUtils;

public class AgentLogoutEvent implements Event {
    private final String eventType;
    private String timestamp;

    public AgentLogoutEvent() {
        this.eventType = EventTypes.AGENT_LOG_OUT.getType();
        this.timestamp = DateUtils.generateTimestamp();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
