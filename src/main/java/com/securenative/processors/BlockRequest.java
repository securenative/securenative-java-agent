package com.securenative.processors;

import com.securenative.filters.BlockRequestFilter;

public class BlockRequest implements Processor {

    @Override
    public void apply() {
        new BlockRequestFilter();
    }
}
