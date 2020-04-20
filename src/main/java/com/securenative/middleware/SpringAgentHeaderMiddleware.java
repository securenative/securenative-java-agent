package com.securenative.middleware;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class SpringAgentHeaderMiddleware extends WebSecurityConfigurerAdapter {
    private final String sessionId;

    public SpringAgentHeaderMiddleware(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    protected void configure(HttpSecurity http) {
        http.addFilterAfter(new AgentHeaderMiddleware(this.sessionId), BasicAuthenticationFilter.class);
    }
}
