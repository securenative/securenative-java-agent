package com.securenative.agent.events;

import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.models.User;
import com.securenative.agent.utils.DateUtils;

public class LoginChallengeEvent implements Event {
    private final String eventType;
    private User user;
    private final String timestamp;

    public LoginChallengeEvent(User user) {
        this.user = user;
        this.eventType = EventTypes.LOG_IN_CHALLENGE.getType();
        this.timestamp = DateUtils.generateTimestamp();
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

    @Override
    public String getTimestamp() {
        return this.timestamp;
    }
}
