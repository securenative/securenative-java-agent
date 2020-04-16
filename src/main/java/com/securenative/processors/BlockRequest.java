package com.securenative.processors;


import filters.BlockRequestFilter;

public class BlockRequest implements Processor {

    @Override
    public void apply() {
        new BlockRequestFilter();
    }
}
