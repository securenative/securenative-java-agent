package com.securenative.models;

public enum InterceptionModule {
    HTTP("http"),
    HTTPS("https");

    private String module;

    InterceptionModule(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }
}
