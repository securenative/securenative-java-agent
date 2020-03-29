package com.securenative.rules;

public class Rule {
    public String name;
    public RuleData data;
    public RuleInterception interception;

    public Rule(String name, RuleData data, RuleInterception interception) {
        this.name = name;
        this.data = data;
        this.interception = interception;
    }

    public Rule(RuleData data, RuleInterception interception) {
        this.data = data;
        this.interception = interception;
    }

    public String getName() {
        return name;
    }

    public RuleData getData() {
        return data;
    }

    public RuleInterception getInterception() {
        return interception;
    }
}
