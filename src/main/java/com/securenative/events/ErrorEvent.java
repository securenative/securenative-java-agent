package com.securenative.events;

import com.securenative.models.EventTypes;
import java.time.ZonedDateTime;

public class ErrorEvent implements Event {
    private String message;
    private String stackTrace;
    private final String eventType;
    private long ts;

    public ErrorEvent(String stackTrace, String message) {
        this.eventType = EventTypes.ERROR.getType();
        this.ts = ZonedDateTime.now().toEpochSecond();
        this.message = message;
        this.stackTrace = stackTrace;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public long getTs() {
        return ts;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
