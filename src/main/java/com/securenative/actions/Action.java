package com.securenative.actions;

import java.util.List;

public class Action {
    public String name;
    public long ttl;
    public long ts;
    public List<String> values;

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
