package com.securenative.agent.events.manager;

import com.securenative.agent.events.Event;
import com.securenative.agent.exceptions.SecureNativeParseException;

import java.io.IOException;

public interface EventManager {
    <T> T sendSync(Class<T> clazz, Event event, String url) throws IOException, SecureNativeParseException;
    void sendAsync(Event event, String url, Boolean retry);
    void startEventsPersist();
    void stopEventsPersist();
}