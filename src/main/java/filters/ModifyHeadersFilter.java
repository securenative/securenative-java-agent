package filters;

import com.securenative.rules.Rule;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ModifyHeadersFilter implements Filter {
    private Rule rule;

    public ModifyHeadersFilter(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpRes = (HttpServletResponse) servletResponse;
        String key = this.rule.data.key;
        String value = this.rule.data.value;

        httpRes.setHeader(key, value);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}
}
