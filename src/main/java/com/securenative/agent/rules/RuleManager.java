package com.securenative.agent.rules;

import com.securenative.agent.processors.ProcessorsFactory;

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
            if (rule.getInterception().getMethod().equals(method)) {
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
            RuleData data = rule.getData();
            RuleInterception interception = rule.getInterception();
            String module = interception.getModule();
            String method = interception.getMethod();
            String processor = interception.getProcessor();

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
