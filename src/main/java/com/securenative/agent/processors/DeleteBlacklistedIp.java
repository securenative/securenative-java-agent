package com.securenative.agent.processors;

import com.securenative.agent.actions.Action;
import com.securenative.agent.actions.ActionList;
import com.securenative.agent.enums.SetType;

import java.util.logging.Logger;

public class DeleteBlacklistedIp extends Processor {
    private static final Logger logger = Logger.getLogger(DeleteBlacklistedIp.class.getName());
    private Action action;

    public DeleteBlacklistedIp(Action action) {
        this.action = action;
    }

    @Override
    public void apply() {
        if (this.action.getValues() != null) {
            this.action.getValues().forEach(value -> {
                logger.fine(String.format("Deleting blacklisted ip: %s", value));
                ActionList.blackList.delete(SetType.IP.toString(), value, null);
            });
        }
    }
}
