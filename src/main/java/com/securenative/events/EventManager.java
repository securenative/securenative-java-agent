package com.securenative.events;


import com.securenative.models.RiskResult;

public interface EventManager {
    RiskResult sendSync(Event event, String requestUrl);
    String sendAgentEvent(Event event, String requestUrl);
    void sendAsync(Event event, String url);
}
