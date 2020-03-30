package com.securenative.models;

public enum EventTypes {
    AGENT_LOG_IN("sn.agent.login"),
    AGENT_LOG_OUT("sn.agent.logout"),
    LOG_IN("sn.user.login"),
    LOG_IN_CHALLENGE("sn.user.login.challenge"),
    LOG_IN_FAILURE("sn.user.login.failure"),
    LOG_OUT("sn.user.logout"),
    SIGN_UP("sn.user.signup"),
    AUTH_CHALLENGE("sn.user.auth.challenge"),
    AUTH_CHALLENGE_SUCCESS("sn.user.auth.challenge.success"),
    AUTH_CHALLENGE_FAILURE("sn.user.auth.challenge.failure"),
    TWO_FACTOR_DISABLE("sn.user.2fa.disable"),
    EMAIL_UPDATE("sn.user.email.update"),
    PASSWORD_RESET("sn.user.password.reset"),
    PASSWORD_RESET_SUCCESS("sn.user.password.reset.success"),
    PASSWORD_UPDATE("sn.user.password.update"),
    PASSWORD_RESET_FAILURE("sn.user.password.reset.failure"),
    USER_INVITE("sn.user.invite"),
    ROLE_UPDATE("sn.user.role.update"),
    PROFILE_UPDATE("sn.user.profile.update"),
    PAGE_VIEW("sn.user.page.view"),
    VERIFY("sn.verify"),
    SDK("sdk"),
    ERROR("error"),
    REQUEST("request"),
    CONFIG("config"),
    ATTACK("attack"),
    METRIC("metric"),
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
