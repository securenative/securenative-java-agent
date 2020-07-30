package com.securenative.agent.processors;

import com.securenative.agent.filters.BlockRequestFilter;

public class BlockRequest extends Processor {
    @Override
    public void apply() {
        new BlockRequestFilter();
    }
}
