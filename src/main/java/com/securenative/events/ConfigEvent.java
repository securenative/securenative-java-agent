package com.securenative.events;

import com.securenative.enums.EventTypes;
import com.securenative.utils.DateUtils;

public class ConfigEvent implements Event {
    private final String eventType;
    private String appName;
    private String timestamp;

    public ConfigEvent(String appName) {
        this.eventType = EventTypes.CONFIG.getType();
        this.appName = appName;
        this.timestamp = DateUtils.generateTimestamp();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getAppName() {
        return appName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
