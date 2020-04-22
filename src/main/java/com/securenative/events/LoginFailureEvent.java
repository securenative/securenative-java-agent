package com.securenative.events;

import com.securenative.models.EventTypes;
import com.securenative.models.User;

public class LoginFailureEvent implements Event{
    private final String eventType;
    private User user;

    public LoginFailureEvent(User user) {
        this.user = user;
        this.eventType = EventTypes.LOG_IN_FAILURE.getType();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }
}
