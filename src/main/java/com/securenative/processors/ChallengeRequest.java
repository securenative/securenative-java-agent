package com.securenative.processors;

import com.securenative.filters.ChallengeFilter;

public class ChallengeRequest implements Processor{
    @Override
    public void apply() {
        new ChallengeFilter();
    }
}
