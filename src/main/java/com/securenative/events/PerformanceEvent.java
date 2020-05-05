package com.securenative.events;

import com.securenative.enums.EventTypes;
import com.securenative.utils.Utils;

public class PerformanceEvent implements Event {
    private final String eventType;
    private String timestamp;

    public PerformanceEvent() {
        this.eventType = EventTypes.PERFORMANCE.getType();
        this.timestamp = Utils.generateTimestamp();
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
