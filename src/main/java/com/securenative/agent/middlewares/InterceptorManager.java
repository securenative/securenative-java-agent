package com.securenative.agent.middlewares;

import com.securenative.agent.SecureNative;

public class InterceptorManager {
    public static void applyModuleInterceptors(SecureNative secureNative) {
        new VerifyRequestMiddleware(secureNative);
        new VerifyWebhookMiddleware(secureNative);
    }

    public static void applyAgentInterceptor(String sessionId) {
        new AgentHeaderMiddleware(sessionId);
    }
}
