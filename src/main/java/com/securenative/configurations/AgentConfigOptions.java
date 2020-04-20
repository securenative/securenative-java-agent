package com.securenative.configurations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.actions.Action;
import com.securenative.rules.Rule;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentConfigOptions {
    @JsonProperty("rules")
    private List<Rule> rules;
    @JsonProperty("actions")
    private List<Action> actions;
    @JsonProperty("ts")
    private long ts;

    public AgentConfigOptions(List<Rule> rules, List<Action> actions, long ts) {
        this.rules = rules;
        this.actions = actions;
        this.ts = ts;
    }

    // Empty constructor for deserialization
    public AgentConfigOptions() {}

    public List<Rule> getRules() {
        return rules;
    }

    public List<Action> getActions() {
        return actions;
    }

    public long getTs() {
        return ts;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
