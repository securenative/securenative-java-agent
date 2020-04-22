package com.securenative.events;

import com.securenative.models.User;

public class UserEvent implements Event {
    private String eventType;
    private User user;

    public UserEvent(String eventType, User user) {
        this.eventType = eventType;
        this.user = user;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }
}
