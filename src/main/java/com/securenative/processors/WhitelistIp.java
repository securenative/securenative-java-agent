package com.securenative.processors;

import com.securenative.Logger;
import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.models.SetType;

public class WhitelistIp {
    private Action action;

    public WhitelistIp(Action action) {
        this.action = action;
    }

    public void apply() {
        if (this.action.getValues() != null) {
            this.action.getValues().forEach(value -> {
                Logger.getLogger().debug(String.format("Whitelisting ip: %s", value));
                ActionList.whitelist.add(SetType.IP.toString(), value, this.action.getTtl());
            });
        }
    }
}
