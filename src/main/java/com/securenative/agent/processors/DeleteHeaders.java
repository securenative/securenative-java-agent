package com.securenative.agent.processors;

import com.securenative.agent.filters.DeleteHeaderFilter;
import com.securenative.agent.rules.Rule;

public class DeleteHeaders extends Processor {
    private Rule rule;

    public DeleteHeaders(Rule rule) {
        this.rule = rule;
    }

    @Override
    public void apply() {
        new DeleteHeaderFilter(this.rule);
    }
}
