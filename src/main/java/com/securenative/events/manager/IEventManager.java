package com.securenative.events.manager;


import com.securenative.events.Event;
import com.securenative.models.RiskResult;

public interface IEventManager {
    RiskResult sendSync(Event event, String requestUrl);
    String sendAgentEvent(Event event, String requestUrl);
    void sendAsync(Event event, String url);
}
