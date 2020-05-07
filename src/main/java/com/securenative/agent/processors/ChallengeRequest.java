package com.securenative.agent.processors;

import com.securenative.agent.filters.ChallengeFilter;

public class ChallengeRequest extends Processor{
    @Override
    public void apply() {
        new ChallengeFilter();
    }
}
