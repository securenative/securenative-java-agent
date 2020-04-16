package com.securenative.events;

import com.securenative.models.EventTypes;

public class ConfigEvent implements Event {
    private String eventType;
    private String hostId;
    private String appName;
    private long ts;

    public ConfigEvent(String hostId, String appName, long ts) {
        this.eventType = EventTypes.CONFIG.getType();
        this.appName = appName;
        this.hostId = hostId;
        this.ts = ts;
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
}
