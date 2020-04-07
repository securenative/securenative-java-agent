package com.securenative.events;

import com.securenative.Logger;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.models.RiskResult;

public class SDKEvents {
    private EventManager eventManager;
    private SecureNativeOptions snOptions;

    public SDKEvents(EventManager eventManager, SecureNativeOptions snOptions) {
        this.eventManager = eventManager;
        this.snOptions = snOptions;
    }

    public void track(Event event) {
        Logger.getLogger().info("Track event call");
        this.eventManager.sendAsync(event, this.snOptions.getApiUrl() + "/track");
    }

    public RiskResult verify(Event event) {
        Logger.getLogger().info("Verify event call");
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/verify");
    }

    public RiskResult flow(long flowId, Event event) { // FOR FUTURE PURPOSES
        Logger.getLogger().info("Flow event call");
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/flow/" + flowId);
    }
}
