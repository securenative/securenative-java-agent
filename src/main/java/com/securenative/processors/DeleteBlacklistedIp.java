package com.securenative.processors;

import com.securenative.Logger;
import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.models.SetType;

public class DeleteBlacklistedIp extends Processor {
    private Action action;

    public DeleteBlacklistedIp(Action action) {
        this.action = action;
    }

    @Override
    public void apply() {
        if (this.action.getValues() != null) {
            this.action.getValues().forEach(value -> {
                Logger.getLogger().debug(String.format("Deleting blacklisted ip: %s", value));
                ActionList.blackList.delete(SetType.IP.toString(), value, null);
            });
        }
    }
}
