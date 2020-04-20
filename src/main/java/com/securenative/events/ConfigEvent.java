package com.securenative.events;

import com.securenative.models.EventTypes;
import com.securenative.utils.Utils;

public class ConfigEvent implements Event {
    private final String eventType;
    private String hostId;
    private String appName;
    private String timestamp;

    public ConfigEvent(String hostId, String appName) {
        this.eventType = EventTypes.CONFIG.getType();
        this.appName = appName;
        this.hostId = hostId;
        this.timestamp = Utils.generateTimestamp();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getHostId() {
        return hostId;
    }

    public String getAppName() {
        return appName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
