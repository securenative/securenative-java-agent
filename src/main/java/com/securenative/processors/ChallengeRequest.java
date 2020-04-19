package com.securenative.processors;

import com.securenative.filters.ChallengeFilter;

public class ChallengeRequest extends Processor{
    @Override
    public void apply() {
        new ChallengeFilter();
    }
}
