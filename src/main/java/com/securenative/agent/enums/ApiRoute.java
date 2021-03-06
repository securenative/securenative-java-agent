package com.securenative.agent.enums;

public enum ApiRoute {
    LOGIN("agent-login"),
    LOGOUT("agent-logout"),
    ERROR("agent-error"),
    CONFIG("agent-config-update"),
    HEARTBEAT("agent-heart-beat"),
    TRACK("track"),
    VERIFY("verify"),
    RISK("risk"),
    FLOW("flow");

    private String route;

    public String getRoute() {
        return route;
    }

    ApiRoute(String apiRoute) {
        this.route = apiRoute;
    }
}
