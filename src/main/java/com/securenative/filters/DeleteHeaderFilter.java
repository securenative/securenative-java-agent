package com.securenative.filters;

import com.securenative.rules.Rule;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteHeaderFilter implements Filter {
    private Rule rule;

    public DeleteHeaderFilter(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpRes = (HttpServletResponse) servletResponse;
        String key = this.rule.data.key;

        if (httpRes.containsHeader(key)) {
            httpRes.setHeader(key, "");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
