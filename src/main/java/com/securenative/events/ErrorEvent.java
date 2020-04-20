package com.securenative.events;

import com.securenative.models.EventTypes;
import com.securenative.utils.Utils;

import java.time.ZonedDateTime;

public class ErrorEvent implements Event {
    private String message;
    private String stackTrace;
    private String eventType;
    private String timestamp;

    public ErrorEvent(String stackTrace, String message) {
        this.eventType = EventTypes.ERROR.getType();
        this.timestamp = Utils.generateTimestamp();
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

    public String getTimestamp() {
        return timestamp;
    }
}
