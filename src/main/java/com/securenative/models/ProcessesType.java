package com.securenative.models;

public enum ProcessesType {
    BLOCK_IP("block_ip"),
    UNBLOCK_IP("unblock_ip"),
    ALLOW_IP("allow_ip");

    ProcessesType(String s) {
        this.type = s;
    }

    public String getType() {
        return type;
    }

    private String type;
}
