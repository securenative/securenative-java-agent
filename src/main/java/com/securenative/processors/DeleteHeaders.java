package com.securenative.processors;

import com.securenative.rules.Rule;
import filters.DeleteHeaderFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class DeleteHeaders implements Processor {
    private Rule rule;

    public DeleteHeaders(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void apply() {
        new DeleteHeaderFilter(this.rule);
    }
}
