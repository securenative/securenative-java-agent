package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.configurations.AgentConfigOptions;

public class AgentLoginResponse {
    @JsonProperty("sessionId")
    public String sessionId;
    @JsonProperty("status")
    public boolean status;
    @JsonProperty("config")
    public AgentConfigOptions config;

    public String getSessionId() {
        return sessionId;
    }

    public boolean isStatus() {
        return status;
    }

    public AgentConfigOptions getConfig() {
        return config;
    }
}
