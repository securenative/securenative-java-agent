package com.securenative.agent.processors;

public enum ProcessorType {
    MODIFY_HEADERS("ModifyHeaders"),
    DELETE_HEADERS("DeleteHeaders"),
    BLACKLIST_IP("BlacklistIp"),
    CHALLENGE_REQUEST("ChallengeRequest"),
    DELETE_BLACKLISTED("DeleteBlacklisted"),
    WHITELIST_IP("WhitelistIp"),
    BLOCK_REQUEST("BlockRequest");

    public String getType() {
        return type;
    }

    private final String type;

    ProcessorType(String process) {
        this.type = process;
    }
}
