package com.securenative.agent.http;


import java.io.IOException;

public interface HttpClient {
    HttpResponse post(String url, String body) throws IOException;
}

