package com.securenative.config;

import com.securenative.enums.FailoverStrategy;

public class SecureNativeConfigurationBuilder {
    /**
     * Api Secret associated with SecureNative account
     */
    private String apiKey;

    /**
     * SecureNative backend API URL
     */
    private String apiUrl;

    /**
     * SecureNative event persistence interval
     */
    private int interval;

    /**
     * Maximum queue capacity
     */
    private int maxEvents;

    /**
     * Event sending timeout
     */
    private int timeout;

    /**
     * Allow automatically track event
     */
    private Boolean autoSend;

    /**
     * Disable SDk, all operation will not take effect
     */
    private Boolean disable;

    /**
     * Default log level
     */
    private String logLevel;

    /**
     * Failover strategy
     */
    private FailoverStrategy failoverStrategy;

    /**
     * Heartbeat delay
     */
    private long heartbeatDelay;

    /**
     * Heartbeat sending period
     */
    private long heartbeatPeriod;

    /**
     * Configuration update delay
     */
    private long configUpdateDelay;

    /**
     * Configuration update sending period
     */
    private long configUpdatePeriod;

    /**
     * Application name
     */
    private String appName;

    private SecureNativeConfigurationBuilder(){ }

    public static SecureNativeConfigurationBuilder defaultConfigBuilder() {
        return new SecureNativeConfigurationBuilder()
                .withApiKey(null)
                .withApiUrl("https://api.securenative.com/collector/api/v1")
                .withInterval(1000)
                .withTimeout(1500)
                .withMaxEvents(1000)
                .withAutoSend(true)
                .withDisable(false)
                .withLogLevel("fatal")
                .withHeartbeatDelay(0)
                .withHeartbeatPeriod(5 * 60 * 1000)
                .withConfigUpdateDelay(0)
                .withConfigUpdatePeriod(5 * 60 * 1000)
                .withAppName(null)
                .withFailoverStrategy(FailoverStrategy.FAIL_OPEN);
    }

    public SecureNativeConfigurationBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public SecureNativeConfigurationBuilder withApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public SecureNativeConfigurationBuilder withInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public SecureNativeConfigurationBuilder withMaxEvents(int maxEvents) {
        this.maxEvents = maxEvents;
        return this;
    }

    public SecureNativeConfigurationBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public SecureNativeConfigurationBuilder withAutoSend(Boolean autoSend) {
        this.autoSend = autoSend;
        return this;
    }

    public SecureNativeConfigurationBuilder withDisable(Boolean disable) {
        this.disable = disable;
        return this;
    }

    public SecureNativeConfigurationBuilder withLogLevel(String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public SecureNativeConfigurationBuilder withFailoverStrategy(FailoverStrategy failoverStrategy) {
        this.failoverStrategy = failoverStrategy;
        return this;
    }

    public SecureNativeConfigurationBuilder withHeartbeatDelay(long heartbeatDelay) {
        this.heartbeatDelay = heartbeatDelay;
        return this;
    }

    public SecureNativeConfigurationBuilder withHeartbeatPeriod(long heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
        return this;
    }

    public SecureNativeConfigurationBuilder withConfigUpdateDelay(long configUpdateDelay) {
        this.configUpdateDelay = configUpdateDelay;
        return this;
    }

    public SecureNativeConfigurationBuilder withConfigUpdatePeriod(long configUpdatePeriod) {
        this.configUpdatePeriod = configUpdatePeriod;
        return this;
    }

    public SecureNativeConfigurationBuilder withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public SecureNativeOptions build() {
        return new SecureNativeOptions(apiKey, apiUrl, interval, maxEvents, timeout, autoSend, disable, logLevel, failoverStrategy, heartbeatDelay, heartbeatPeriod, configUpdateDelay, configUpdatePeriod, appName);
    }
}
