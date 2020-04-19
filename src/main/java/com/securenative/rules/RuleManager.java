package com.securenative.rules;

import com.securenative.processors.*;

import java.util.ArrayList;
import java.util.List;

public class RuleManager {
    private static List<Rule> rules = new ArrayList<>();

    public List<Rule> getAllRules() {
        return rules;
    }

    public List<Rule> getRules(String method) {
        List<Rule> r = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.interception.method.equals(method)) {
                r.add(rule);
            }
        }

        return r;
    }

    public void registerRule(Rule rule) {
        rules.add(rule);
    }

    public void clean() {
        rules.clear();
    }

    public void enforceRules(List<Rule> rules) {
        // clean previous rules
        clean();

        for (Rule rule : rules) {
            RuleData data = rule.data;
            RuleInterception interception = rule.interception;
            String module = interception.module;
            String method = interception.method;
            String processor = interception.processor;

            String[] m = method.split(":");
            String func = "";
            if (m != null && m.length > 0) {
                func = m[0];
            }

            Rule r = new Rule(data, new RuleInterception(module, func, processor));
            ProcessorsFactory.getRuleProcessor(processor, r).apply();
            this.registerRule(rule);
        }
    }
}
