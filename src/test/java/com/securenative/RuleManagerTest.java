package com.securenative;

import com.securenative.rules.Rule;
import com.securenative.rules.RuleData;
import com.securenative.rules.RuleInterception;
import com.securenative.rules.RuleManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RuleManagerTest {
    @Test
    public void registerRules() {
        RuleManager ruleManager = new RuleManager();
        RuleData data = new RuleData("block", "10.0.0.1");
        RuleInterception interception = new RuleInterception("spring", "filter", "block");
        Rule rule = new Rule("test", data, interception);

        Assert.assertEquals(0, ruleManager.getAllRules().size());
        ruleManager.registerRule(rule);
        Assert.assertEquals(1, ruleManager.getAllRules().size());
    }

    @Test
    public void cleanRules() {
        RuleManager ruleManager = new RuleManager();

        Assert.assertEquals(1, ruleManager.getAllRules().size());
        ruleManager.clean();
        Assert.assertEquals(0, ruleManager.getAllRules().size());
    }

    @Test
    public void getRules() {
        RuleManager ruleManager = new RuleManager();
        RuleData data = new RuleData("block", "10.0.0.1");
        RuleInterception interception = new RuleInterception("spring", "filter", "block");
        Rule rule = new Rule("test", data, interception);
        ruleManager.registerRule(rule);

        List<Rule> rules = ruleManager.getRules("filter");
        Assert.assertEquals(1, rules.size());
    }
}
