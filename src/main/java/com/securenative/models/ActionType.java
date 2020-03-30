package com.securenative.models;

public enum ActionType {
        ALLOW("allow"),
        BLOCK("block"),
        CHALLENGE("challenge"),
        BLOCK_IP("block_ip"),
        BLOCK_USER("block_user"),
        REDIRECT_IP("redirect_ip"),
        REDIRECT_USER("redirect_user"),
        REDIRECT("redirect"),
        MFA("mfa");

        private String type;

        public String getType() {
                return type;
        }

        ActionType(String actionType) {
                this.type = actionType;
        }
}
