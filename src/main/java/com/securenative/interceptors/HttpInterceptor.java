package com.securenative.interceptors;

import com.securenative.SecureNative;

public class HttpInterceptor {
    private SecureNative secureNative;

    public HttpInterceptor(SecureNative secureNative) {
        this.secureNative = secureNative;
    }
}
