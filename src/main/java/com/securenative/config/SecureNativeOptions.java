package com.securenative.config;

import com.securenative.enums.FailoverStrategy;

public class SecureNativeOptions {
    /**
     * Api Secret associated with SecureNative account
     */
    private String apiKey;

    /**
     * SecureNative backend API URL
     */
    private final String apiUrl;

    /**
     * Application name
     */
    private String appName;

    /**
     * SecureNative event persistence interval
     */
    private final int interval;

    /**
     * Maximum queue capacity
     */
    private final int maxEvents;

    /**
     * Event sending timeout
     */
    private final int timeout;

    /**
     * Allow automatically track event
     */
    private final Boolean autoSend;

    /**
     * Disable SDk, all operation will not take effect
     */
    private final Boolean disable;

    /**
     * Default log level
     */
    private final String logLevel;

    /**
     * Failover strategy
     */
    private final FailoverStrategy failoverStrategy;

    /**
    * Heartbeat delay
     */
    private final long heartbeatDelay;

    /**
     * Heartbeat sending period
     */
    private final long heartbeatPeriod;

    /**
     * Configuration update delay
     */
    private final long configUpdateDelay;

    /**
     * Configuration update sending period
     */
    private final long configUpdatePeriod;

    /**
     * Minimum java version supported
     */
    private final String minSupportedVersion;

    public SecureNativeOptions(String apiKey, String apiUrl, int interval, int maxEvents, int timeout, boolean autoSend, boolean disable, String logLevel, FailoverStrategy failoverStrategy, long heartbeatDelay, long heartbeatPeriod, long configUpdateDelay, long configUpdatePeriod, String appName) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.appName = appName;
        this.interval = interval;
        this.maxEvents = maxEvents;
        this.timeout = timeout;
        this.autoSend = autoSend;
        this.disable = disable;
        this.logLevel = logLevel;
        this.failoverStrategy = failoverStrategy;
        this.heartbeatDelay = heartbeatDelay;
        this.heartbeatPeriod = heartbeatPeriod;
        this.configUpdateDelay = configUpdateDelay;
        this.configUpdatePeriod = configUpdatePeriod;
        this.minSupportedVersion = "8";
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public int getInterval() {
        return interval;
    }

    public int getMaxEvents() {
        return maxEvents;
    }

    public int getTimeout() {
        return timeout;
    }

    public Boolean getAutoSend() {
        return autoSend;
    }

    public Boolean getDisabled() {
        return disable;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public FailoverStrategy getFailoverStrategy() {
        return failoverStrategy;
    }

    public Boolean getDisable() {
        return disable;
    }

    public long getHeartbeatDelay() {
        return heartbeatDelay;
    }

    public long getHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    public long getConfigUpdateDelay() {
        return configUpdateDelay;
    }

    public long getConfigUpdatePeriod() {
        return configUpdatePeriod;
    }

    public String getAppName() {
        return appName;
    }

    public String getMinSupportedVersion() {
        return minSupportedVersion;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
