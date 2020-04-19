package com.securenative.interceptors;

import com.securenative.SecureNative;
import com.securenative.middleware.VerifyRequestMiddleware;
import com.securenative.middleware.VerifyWebhookMiddleware;

public class InterceptorManager {
    public static void applyModuleInterceptors(SecureNative secureNative) {
        new VerifyRequestMiddleware(secureNative);
        new VerifyWebhookMiddleware(secureNative);
        new HttpInterceptor(secureNative);
        new HttpsInterceptor(secureNative);
    }

    public static void applyAgentInterceptor(String sessionId) {
        new AgentHeaderInterceptor(sessionId);
    }
}
