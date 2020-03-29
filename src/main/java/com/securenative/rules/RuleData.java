package com.securenative.rules;

public class RuleData {
    public String key;
    public String value;

    public RuleData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
