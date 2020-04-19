package com.securenative.processors;

import com.securenative.filters.BlockRequestFilter;

public class BlockRequest extends Processor {
    @Override
    public void apply() {
        new BlockRequestFilter();
    }
}
