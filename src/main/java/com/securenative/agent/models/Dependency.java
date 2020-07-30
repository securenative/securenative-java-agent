package com.securenative.agent.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dependency {
    @JsonProperty("key")
    private String name;
    @JsonProperty("value")
    private String version;

    public Dependency(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }
}
