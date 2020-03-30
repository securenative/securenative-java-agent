package com.securenative.interceptors;

import com.securenative.module.ModuleManager;
import com.securenative.SecureNative;

public class InterceptorManager {
    public static void applyModuleInterceptors(ModuleManager moduleManager, SecureNative secureNative) {
        if (moduleManager.getFramework().toLowerCase().contains("spring")) {
            new SpringInterceptor(secureNative);
            new HttpInterceptor(secureNative);
            new HttpsInterceptor(secureNative);
        }
        new SpringInterceptor(secureNative);  // Default interceptor
        new HttpInterceptor(secureNative);
        new HttpsInterceptor(secureNative);
    }

    public static void applyAgentInterceptor(String sessionId) {
        new AgentHeaderInterceptor(sessionId);
    }
}
