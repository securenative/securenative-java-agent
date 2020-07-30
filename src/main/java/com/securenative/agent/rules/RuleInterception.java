package com.securenative.agent.rules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleInterception {
    @JsonProperty("module")
    private String module;
    @JsonProperty("method")
    private String method;
    @JsonProperty("processor")
    private String processor;

    public RuleInterception(String module, String method, String processor) {
        this.module = module;
        this.method = method;
        this.processor = processor;
    }

    // Empty constructor for deserialization
    public RuleInterception() {}

    public String getModule() {
        return module;
    }

    public String getMethod() {
        return method;
    }

    public String getProcessor() {
        return processor;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }
}