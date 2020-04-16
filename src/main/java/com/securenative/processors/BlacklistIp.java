package com.securenative.processors;

import com.securenative.Logger;
import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.models.SetType;

public class BlacklistIp implements Processor {
    private Action action;

    public BlacklistIp(Action action) {
        this.action = action;
    }

    @Override
    public void apply() {
        if (this.action.getValues().size() > 0) {
            this.action.getValues().forEach(value -> {
                Logger.getLogger().debug(String.format("Blacklisting ip: %s", value));
                ActionList.blackList.add(SetType.IP.toString(), value, this.action.getTtl());
            });
        }
    }
}
