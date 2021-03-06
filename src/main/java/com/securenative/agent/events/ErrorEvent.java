package com.securenative.agent.events;

import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.utils.DateUtils;

public class ErrorEvent implements Event {
    private String message;
    private String stackTrace;
    private final String eventType;
    private String timestamp;
 

    public ErrorEvent(String stackTrace, String message) {
        this.eventType = EventTypes.ERROR.getType();
        this.timestamp = DateUtils.generateTimestamp();
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
