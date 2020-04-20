package com.securenative.interceptors;

import com.securenative.SecureNative;
import com.securenative.middleware.AgentHeaderMiddleware;
import com.securenative.middleware.SpringAgentHeaderMiddleware;
import com.securenative.middleware.VerifyRequestMiddleware;
import com.securenative.middleware.VerifyWebhookMiddleware;

public class InterceptorManager {
    public static void applyModuleInterceptors(SecureNative secureNative) {
        new VerifyRequestMiddleware(secureNative);
        new VerifyWebhookMiddleware(secureNative);
    }

    public static void applyAgentInterceptor(String framework, String sessionId) {
        if (framework.toLowerCase().equals("spring")) {
            new SpringAgentHeaderMiddleware(sessionId);
        } else {
            new AgentHeaderMiddleware(sessionId);
        }
    }
}
