package com.securenative.processors;


import filters.ChallengeFilter;

public class ChallengeRequest implements Processor{
    @Override
    public void apply() {
        new ChallengeFilter();
    }
}
