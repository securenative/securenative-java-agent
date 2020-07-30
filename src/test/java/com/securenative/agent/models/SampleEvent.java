package com.securenative.agent.models;

import com.securenative.agent.events.Event;
import com.securenative.agent.utils.DateUtils;

public class SampleEvent implements Event {
    private final String eventType = "custom-event";
    private final String timestamp = DateUtils.generateTimestamp();

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }
}
