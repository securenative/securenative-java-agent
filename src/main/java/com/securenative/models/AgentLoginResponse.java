package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.configurations.AgentConfigOptions;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentLoginResponse {
    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("config")
    private AgentConfigOptions config;

    public AgentLoginResponse(String sessionId, boolean status, AgentConfigOptions config) {
        this.sessionId = sessionId;
        this.status = status;
        this.config = config;
    }

    // Empty constructor for deserialization
    public AgentLoginResponse() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isStatus() {
        return status;
    }

    public AgentConfigOptions getConfig() {
        return config;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setConfig(AgentConfigOptions config) {
        this.config = config;
    }
}
