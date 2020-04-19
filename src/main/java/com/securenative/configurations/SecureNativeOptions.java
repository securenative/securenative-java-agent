package com.securenative.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SecureNativeOptions {
    @JsonProperty("apiUrl")
    private String apiUrl;
    @JsonProperty("interval")
    private int interval;
    @JsonProperty("maxEvents")
    private long maxEvents;
    @JsonProperty("timeout")
    private long timeout;
    @JsonProperty("autoSend")
    private Boolean autoSend;
    @JsonProperty("isSdkEnabled")
    private Boolean isSdkEnabled;
    @JsonProperty("debugMode")
    private Boolean debugMode;
    @JsonProperty("appName")
    private String appName;
    @JsonProperty("apiKey")
    private String apiKey;
    @JsonProperty("isAgentDisable")
    private Boolean isAgentDisable;
    @JsonProperty("minSupportedVersion")
    private String minSupportedVersion;
    @JsonProperty("hostId")
    private String hostId;
    @JsonProperty("heartBeatInterval")
    private long heartBeatInterval;
    @JsonProperty("cookieName")
    private String cookieName;

    public SecureNativeOptions() {
        this.autoSend = true;
        this.isSdkEnabled = true;
        this.debugMode = false;
    }

    public SecureNativeOptions(String apiKey, Boolean isAgentDisable, String appName, String apiUrl, int interval, long maxEvents, int timeout, boolean autoSend, boolean isSdkEnabled, boolean debugMode, String hostId, long heartBeatInterval) {
        this.interval = interval;
        this.heartBeatInterval = heartBeatInterval;
        this.maxEvents = maxEvents;
        this.apiUrl = apiUrl;
        this.timeout = timeout;
        this.autoSend = autoSend;
        this.isSdkEnabled = isSdkEnabled;
        this.debugMode = debugMode;
        this.appName = appName;
        this.apiKey = apiKey;
        this.isAgentDisable = isAgentDisable;
        this.hostId = hostId;
    }

    public Boolean isAgentDisable() {
        return isAgentDisable;
    }

    public SecureNativeOptions(String apiUrl, int interval, long maxEvents, int timeout) {
        this.interval = interval;
        this.maxEvents = maxEvents;
        this.apiUrl = apiUrl;
        this.timeout = timeout;
        this.autoSend = true;
        this.isSdkEnabled = true;
        this.debugMode = false;

    }

    public String getAppName() {
        return appName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getMaxEvents() {
        return maxEvents;
    }

    public void setMaxEvents(long maxEvents) {
        this.maxEvents = maxEvents;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Boolean isAutoSend() {
        return autoSend;
    }

    public void setAutoSend(Boolean autoSend) {
        this.autoSend = autoSend;
    }

    public Boolean getSdkEnabled() {
        return isSdkEnabled;
    }

    public void setSdkEnabled(Boolean sdkEnabled) {
        isSdkEnabled = sdkEnabled;
    }

    public Boolean getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(Boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setAgentDisable(Boolean agentDisable) {
        isAgentDisable = agentDisable;
    }

    public Boolean getAutoSend() {
        return autoSend;
    }

    public Boolean getAgentDisable() {
        return isAgentDisable;
    }

    public void setMinSupportedVersion(String minSupportedVersion) {
        this.minSupportedVersion = minSupportedVersion;
    }

    public String getMinSupportedVersion() {
        return minSupportedVersion;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public long getHeartBeatInterval() {
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(long heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
