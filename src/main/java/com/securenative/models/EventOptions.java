package com.securenative.models;

import java.util.Map;

public class EventOptions {
    private String ip;
    private String userAgent;
    private String eventType;
    private String remoteIp;
    private  User user;
    private String device;
    private Map<String, String> params;

    public EventOptions(String ip, String userAgent, String eventType, String remoteIp, User user, String device, Map<String, String> params) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.eventType = eventType;
        this.remoteIp = remoteIp;
        this.user = user;
        this.device = device;
        this.params = params;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getEventType() {
        return eventType;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public User getUser() {
        return user;
    }

    public String getDevice() {
        return device;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
