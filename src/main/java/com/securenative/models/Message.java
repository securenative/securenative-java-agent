package com.securenative.models;

import com.securenative.events.Event;

public class Message {
    private Event event;
    private String url;

    public Message(Event event, String url) {
        this.event = event;
        this.url = url;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
