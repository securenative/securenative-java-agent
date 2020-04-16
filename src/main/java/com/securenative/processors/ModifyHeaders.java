package com.securenative.processors;

import com.securenative.rules.Rule;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ModifyHeaders implements Processor, Filter {
    private Rule rule;

    public ModifyHeaders(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader(this.rule.data.getKey(), this.rule.data.getValue());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void apply() {

    }
}
