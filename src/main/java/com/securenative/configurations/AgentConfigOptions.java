package com.securenative.configurations;

import com.securenative.actions.Action;
import com.securenative.rules.Rule;

import java.util.List;

public class AgentConfigOptions {
    public List<Rule> rules;
    public List<Action> actions;
    public long ts;

    public List<Rule> getRules() {
        return rules;
    }

    public List<Action> getActions() {
        return actions;
    }

    public long getTs() {
        return ts;
    }
}
