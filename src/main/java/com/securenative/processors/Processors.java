package com.securenative.processors;

public enum Processors {
    MODIFY_HEADERS("ModifyHeaders"),
    DELETE_HEADERS("DeleteHeaders"),
    BLACKLIST_IP("BlacklistIp"),
    CHALLENGE_REQUEST("ChallengeRequest"),
    DELETE_BLACKLISTED("DeleteBlacklisted"),
    WHITELIST_IP("WhitelistIp"),
    BLOCK_REQUEST("BlockRequest");

    public String getProcess() {
        return process;
    }

    private final String process;

    Processors(String process) {
        this.process = process;
    }
}
