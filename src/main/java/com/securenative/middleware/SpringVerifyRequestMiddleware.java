package com.securenative.middleware;

import com.securenative.SecureNative;

import javax.servlet.*;
import java.io.IOException;

public class SpringVerifyRequestMiddleware implements Filter {
    private SecureNative secureNative;

    public SpringVerifyRequestMiddleware(SecureNative secureNative) {
        this.secureNative = secureNative;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    // TODO add verifyRequest
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
