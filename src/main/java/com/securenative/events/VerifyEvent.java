package com.securenative.events;

import com.securenative.models.EventTypes;

public class VerifyEvent implements Event {
    private final String eventType;

    public VerifyEvent() {
        this.eventType = EventTypes.VERIFY.getType();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }
}
