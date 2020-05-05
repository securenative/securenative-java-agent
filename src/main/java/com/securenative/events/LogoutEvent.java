package com.securenative.events;

import com.securenative.enums.EventTypes;
import com.securenative.models.User;
import com.securenative.utils.Utils;

public class LogoutEvent implements Event {
    private final String eventType;
    private User user;
    private final String timestamp;

    public LogoutEvent(User user) {
        this.eventType = EventTypes.LOG_OUT.getType();
        this.user = user;
        this.timestamp = Utils.generateTimestamp();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    @Override
    public String getTimestamp() {
        return this.timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
