package com.securenative.rules;

import java.util.ArrayList;
import java.util.List;

public class RuleManager {
    private static List<Rule> rules = new ArrayList<>();

    public List<Rule> getRules(String method, String listener) {
        List<Rule> r = new ArrayList<>();
        for (Rule rule : rules) {
            // TODO filter rules according to
        }

        return r;
    }

    private void registerRule(Rule rule) {
        rules.add(rule);
    }

    public static void clean() {
        rules.clear();
    }

    public static void enforceRules(List<Rule> rules) {
        // clean previous rules
        clean();

        for (Rule rule : rules) {
            RuleData data = rule.data;
            RuleInterception interception = rule.interception;
            String module = interception.module;
            String method = interception.method;
            String processor = interception.processor;


            String[] m = method.split(":");
            String func = m[0];
            String listener = m[1];

            Rule r = new Rule(data, new RuleInterception(module, func, processor));  // TODO add process session
        }
    }
}
