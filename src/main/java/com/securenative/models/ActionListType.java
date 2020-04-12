package com.securenative.models;

public enum ActionListType {
    WHITELIST("WhiteList"),
    BLACKLIST("BlackList");

    ActionListType(String s) {
        this.type = s;
    }

    public String getType() {
        return type;
    }

    private String type;
}
