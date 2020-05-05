package com.securenative.middlewares;

import com.securenative.SecureNative;
import com.securenative.utils.SignatureUtils;
import com.securenative.utils.Utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class VerifyWebhookMiddleware implements Filter {
    private static final Logger logger = Logger.getLogger(VerifyWebhookMiddleware.class.getName());
    private SecureNative secureNative;
    private final String SIGNATURE_KEY = "x-securenative";

    public VerifyWebhookMiddleware(SecureNative secureNative) {
        this.secureNative = secureNative;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest == null) {
            return;
        }
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String signature = "";
        if (!Utils.isNullOrEmpty(req.getHeader(SIGNATURE_KEY))) {
            signature = req.getHeader(SIGNATURE_KEY);
        }
        String payload = getBody(servletRequest);
        if (SignatureUtils.isValidSignature(payload, signature, this.secureNative.getApiKey())) {
            filterChain.doFilter(req, res);
            return;
        }
        logger.fine("Request have been blocked due to incompatible signature");
        res.sendError(401, "Unauthorized");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    private String getBody(ServletRequest servletRequest) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = servletRequest.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return stringBuilder.toString();
    }
}
