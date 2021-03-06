package com.securenative.agent.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.agent.actions.Action;
import com.securenative.agent.utils.DateUtils;
import com.securenative.agent.rules.Rule;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentConfigOptions {
    @JsonProperty("rules")
    private List<Rule> rules;
    @JsonProperty("actions")
    private List<Action> actions;
    @JsonProperty("timestamp")
    private String timestamp;

    public AgentConfigOptions(List<Rule> rules, List<Action> actions, String timestamp) {
        this.rules = rules;
        this.actions = actions;
        this.timestamp = timestamp;
    }

    // Empty constructor for deserialization
    public AgentConfigOptions() {
        this.timestamp = DateUtils.generateTimestamp();
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<Action> getActions() {
        return actions;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
