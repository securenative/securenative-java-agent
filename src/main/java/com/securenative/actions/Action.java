package com.securenative.actions;

import java.util.List;

public class Action {
    private String name;
    private long ttl;
    private long ts;
    private List<String> values;

    public Action(String name, long ttl, long ts, List<String> values) {
        this.name = name;
        this.ttl = ttl;
        this.ts = ts;
        this.values = values;
    }

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
}
