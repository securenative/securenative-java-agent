package com.securenative.agent.events.manager;

import com.securenative.agent.SecureNative;
import com.securenative.agent.config.SecureNativeOptions;
import com.securenative.agent.enums.ApiRoute;
import com.securenative.agent.enums.RiskLevel;
import com.securenative.agent.events.Event;
import com.securenative.agent.events.SDKEvent;
import com.securenative.agent.exceptions.SecureNativeSDKException;
import com.securenative.agent.models.EventOptions;
import com.securenative.agent.models.VerifyResult;
import com.securenative.agent.enums.FailoverStrategy;

import java.util.logging.Logger;

public class ApiManagerImpl implements ApiManager {
    private final EventManager eventManager;
    private final SecureNativeOptions options;
    public static final Logger logger = Logger.getLogger(SecureNative.class.getName());

    public ApiManagerImpl(EventManager eventManager, SecureNativeOptions options) throws SecureNativeSDKException {
        this.eventManager = eventManager;
        this.options = options;
    }

    @Override
    public void track(EventOptions eventOptions) {
        logger.info("Track event call");
        String requestUrl = String.format("%s/%s", this.options.getApiUrl(), ApiRoute.TRACK.getRoute());
        Event event = new SDKEvent(eventOptions, this.options);
        this.eventManager.sendAsync(event, requestUrl, true);
    }

    @Override
    public VerifyResult verify(EventOptions eventOptions) {
        logger.info("Verify event call");
        String requestUrl = String.format("%s/%s", this.options.getApiUrl(), ApiRoute.VERIFY.getRoute());
        Event event = new SDKEvent(eventOptions, this.options);
        try {
            return this.eventManager.sendSync(VerifyResult.class , event, requestUrl);
        } catch (Exception ex) {
            logger.severe(String.format("Failed to call verify; %s", ex));
            return this.options.getFailoverStrategy() == FailoverStrategy.FAIL_OPEN ?
                    new VerifyResult(RiskLevel.LOW, 0, new String[0])
                    : new VerifyResult(RiskLevel.HIGH, 1, new String[0]);
        }
    }
}
