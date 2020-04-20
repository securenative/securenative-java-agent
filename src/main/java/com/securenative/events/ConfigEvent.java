package com.securenative.events;

import com.securenative.models.EventTypes;

import java.time.ZonedDateTime;

public class ConfigEvent implements Event {
    private final String eventType;
    private String hostId;
    private String appName;
    private long ts;

    public ConfigEvent(String hostId, String appName) {
        this.eventType = EventTypes.CONFIG.getType();
        this.appName = appName;
        this.hostId = hostId;
        this.ts = ZonedDateTime.now().toEpochSecond();
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

    public long getTs() {
        return ts;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
