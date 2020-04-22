package com.securenative.events;

import com.securenative.models.EventTypes;
import com.securenative.models.User;

public class LogoutEvent implements Event {
    private final String eventType;
    private User user;

    public LogoutEvent(User user) {
        this.eventType = EventTypes.LOG_OUT.getType();
        this.user = user;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
