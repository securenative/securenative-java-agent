package com.securenative.processors;

import com.securenative.rules.Rule;
import com.securenative.filters.ModifyHeadersFilter;

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
