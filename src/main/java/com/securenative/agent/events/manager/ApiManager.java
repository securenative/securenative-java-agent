package com.securenative.agent.events.manager;

import com.securenative.agent.models.EventOptions;
import com.securenative.agent.models.VerifyResult;

public interface ApiManager {
    void track(EventOptions eventOptions);
    VerifyResult verify(EventOptions eventOptions);
}
