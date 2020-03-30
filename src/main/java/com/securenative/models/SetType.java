package com.securenative.models;

public enum SetType {
    IP("ip"),
    PATH("path"),
    USER("user"),
    COUNTRY("country");

    public String getType() {
        return type;
    }

    private String type;

    SetType(String type) {
        this.type = type;
    }
}
