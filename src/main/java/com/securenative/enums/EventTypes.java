package com.securenative.enums;

public enum EventTypes {
    AGENT_LOG_IN("sn.agent.login"),
    AGENT_LOG_OUT("sn.agent.logout"),
    LOG_IN("sn.user.login"),
    LOG_IN_CHALLENGE("sn.user.login.challenge"),
    LOG_IN_FAILURE("sn.user.login.failure"),
    LOG_OUT("sn.user.logout"),
    SDK("sdk"),
    ERROR("error"),
    REQUEST("request"),
    CONFIG("config"),
    HEARTBEAT("heartbeat"),
    PERFORMANCE("performance");

    public String getType() {
        return type;
    }

    private String type;

    EventTypes(String eventType) {
        this.type = eventType;
    }
}
