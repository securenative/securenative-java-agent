package com.securenative.agent.actions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Action {
    @JsonProperty("name")
    private String name;
    @JsonProperty("ttl")
    private long ttl;
    @JsonProperty("ts")
    private long ts;
    @JsonProperty("values")
    private List<String> values;

    public Action(String name, long ttl, long ts, List<String> values) {
        this.name = name;
        this.ttl = ttl;
        this.ts = ts;
        this.values = values;
    }

    // Empty constructor for deserialization
    public Action() {}

    public String getName() {
        return name;
    }

    public long getTtl() {
        return ttl;
    }

    public long getTs() {
        return ts;
    }

    public List<String> getValues() {
        return values;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
