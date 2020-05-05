package com.securenative.middlewares;

import com.securenative.SecureNative;

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
