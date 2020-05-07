package com.securenative.agent.events;

import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.utils.DateUtils;

public class PerformanceEvent implements Event {
    private final String eventType;
    private String timestamp;

    public PerformanceEvent() {
        this.eventType = EventTypes.PERFORMANCE.getType();
        this.timestamp = DateUtils.generateTimestamp();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }
}
