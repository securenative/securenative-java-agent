package com.securenative.processors;

import com.securenative.rules.Rule;
import com.securenative.filters.DeleteHeaderFilter;

public class DeleteHeaders implements Processor {
    private Rule rule;

    public DeleteHeaders(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void apply() {
        new DeleteHeaderFilter(this.rule);
    }
}
