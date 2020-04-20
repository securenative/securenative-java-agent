package com.securenative.rules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rule {
    @JsonProperty("name")
    private String name;
    @JsonProperty("data")
    private RuleData data;
    @JsonProperty("interception")
    private RuleInterception interception;

    public Rule(String name, RuleData data, RuleInterception interception) {
        this.name = name;
        this.data = data;
        this.interception = interception;
    }

    public Rule(RuleData data, RuleInterception interception) {
        this.data = data;
        this.interception = interception;
    }

    // Empty constructor for deserialization
    public Rule() {}

    public String getName() {
        return name;
    }

    public RuleData getData() {
        return data;
    }

    public RuleInterception getInterception() {
        return interception;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(RuleData data) {
        this.data = data;
    }

    public void setInterception(RuleInterception interception) {
        this.interception = interception;
    }
}
