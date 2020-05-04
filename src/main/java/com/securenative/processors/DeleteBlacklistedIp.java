package com.securenative.processors;

import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.models.SetType;
import com.securenative.utils.Logger;

public class DeleteBlacklistedIp extends Processor {
    private static final Logger logger = Logger.getLogger(DeleteBlacklistedIp.class);
    private Action action;

    public DeleteBlacklistedIp(Action action) {
        this.action = action;
    }

    @Override
    public void apply() {
        if (this.action.getValues() != null) {
            this.action.getValues().forEach(value -> {
                logger.debug(String.format("Deleting blacklisted ip: %s", value));
                ActionList.blackList.delete(SetType.IP.toString(), value, null);
            });
        }
    }
}
