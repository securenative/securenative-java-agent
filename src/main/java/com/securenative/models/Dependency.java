package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dependency {
    private @JsonProperty("key") String name;
    private @JsonProperty("value") String version;

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
