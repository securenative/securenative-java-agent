package com.securenative.agent.events;

import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.models.User;
import com.securenative.agent.utils.DateUtils;

public class LogoutEvent implements Event {
    private final String eventType;
    private User user;
    private final String timestamp;

    public LogoutEvent(User user) {
        this.eventType = EventTypes.LOG_OUT.getType();
        this.user = user;
        this.timestamp = DateUtils.generateTimestamp();
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
