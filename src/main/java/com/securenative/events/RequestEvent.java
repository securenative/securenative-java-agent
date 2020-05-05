package com.securenative.events;

import com.securenative.enums.EventTypes;
import com.securenative.models.RequestOptions;
import com.securenative.utils.Utils;

public class RequestEvent implements Event {
    private final String eventType;
    private String url;
    private String body;
    private final String timestamp;

    public RequestEvent(RequestOptions options) {
        this.eventType = EventTypes.REQUEST.getType();
        this.url = options.getUrl();
        this.body = options.getBody();
        this.timestamp = Utils.generateTimestamp();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
