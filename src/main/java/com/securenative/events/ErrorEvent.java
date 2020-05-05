package com.securenative.events;

import com.securenative.enums.EventTypes;
import com.securenative.utils.Utils;

public class ErrorEvent implements Event {
    private String message;
    private String stackTrace;
    private final String eventType;
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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
