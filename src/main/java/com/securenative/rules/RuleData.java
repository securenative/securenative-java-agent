package com.securenative.rules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleData {
    @JsonProperty("key")
    public String key;
    @JsonProperty("value")
    public String value;

    public RuleData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Empty constructor for deserialization
    public RuleData() {}

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
