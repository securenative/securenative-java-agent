package com.securenative.processors;

import com.securenative.rules.Rule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class DeleteHeaders implements Processor, ClientHttpRequestInterceptor {
    private Rule rule;

    public DeleteHeaders(Rule rule) {
        this.rule = rule;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpHeaders headers = httpRequest.getHeaders();
        headers.remove(this.rule.data.value);

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

    @Override
    public void apply() {

    }
}
