package com.securenative.agent.processors;

import com.securenative.agent.filters.ModifyHeadersFilter;
import com.securenative.agent.rules.Rule;

public class ModifyHeaders extends Processor {
    private Rule rule;

    public ModifyHeaders(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void apply() {
        new ModifyHeadersFilter(this.rule);
    }
}
