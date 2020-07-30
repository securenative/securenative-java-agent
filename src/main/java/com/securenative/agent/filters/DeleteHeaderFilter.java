package com.securenative.agent.filters;

import com.securenative.agent.rules.Rule;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteHeaderFilter implements Filter {
    private Rule rule;

    public DeleteHeaderFilter(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpRes = (HttpServletResponse) servletResponse;
        String key = this.rule.getData().key;

        if (httpRes.containsHeader(key)) {
            httpRes.setHeader(key, "");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
