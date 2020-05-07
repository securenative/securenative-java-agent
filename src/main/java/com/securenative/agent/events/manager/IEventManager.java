package com.securenative.agent.events.manager;


import com.securenative.agent.events.Event;
import com.securenative.agent.models.RiskResult;

public interface IEventManager {
    RiskResult sendSync(Event event, String requestUrl);
    String sendAgentEvent(Event event, String requestUrl);
    void sendAsync(Event event, String url);
}
