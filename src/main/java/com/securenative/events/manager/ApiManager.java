package com.securenative.events.manager;

import com.securenative.models.EventOptions;
import com.securenative.models.VerifyResult;

public interface ApiManager {
    void track(EventOptions eventOptions);
    VerifyResult verify(EventOptions eventOptions);
}
