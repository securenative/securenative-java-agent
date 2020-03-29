package com.securenative.rules;

public class RuleInterception {
    public String module;
    public String method;
    public String processor;

    public RuleInterception(String module, String method, String processor) {
        this.module = module;
        this.method = method;
        this.processor = processor;
    }

    public String getModule() {
        return module;
    }

    public String getMethod() {
        return method;
    }

    public String getProcessor() {
        return processor;
    }
}