package com.securenative.agent.processors;

import com.securenative.agent.actions.Action;
import com.securenative.agent.rules.Rule;

import static com.securenative.agent.processors.ProcessorType.*;

public class ProcessorsFactory {
    public static Processor getRuleProcessor(String processor, Rule rule) {
        if (processor.equals(DELETE_HEADERS.getType())) {
            return new DeleteHeaders(rule);
        } else if (processor.equals(MODIFY_HEADERS.getType())) {
            return new ModifyHeaders(rule);
        } else if (processor.equals(BLOCK_REQUEST.getType())) {
            return new BlockRequest();
        } else if (processor.equals(CHALLENGE_REQUEST.getType())) {
            return new ChallengeRequest();
        } else {
            return new Processor();
        }
    }

    public static Processor getActionProcessor(String processor, Action action) {
        if (processor.equals(BLACKLIST_IP.getType())) {
            return new BlacklistIp(action);
        } else if (processor.equals(DELETE_BLACKLISTED.getType())) {
            return new DeleteBlacklistedIp(action);
        } else if (processor.equals(WHITELIST_IP.getType())) {
            return new WhitelistIp(action);
        } else {
            return new Processor();
        }
    }
}
