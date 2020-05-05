package com.securenative.processors;

import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.enums.SetType;

import java.util.logging.Logger;

public class WhitelistIp extends Processor {
    private static final Logger logger = Logger.getLogger(WhitelistIp.class.getName());
    private Action action;

    public WhitelistIp(Action action) {
        this.action = action;
    }

    @Override
    public void apply() {
        if (this.action.getValues() != null) {
            this.action.getValues().forEach(value -> {
                logger.fine(String.format("Whitelisting ip: %s", value));
                ActionList.whitelist.add(SetType.IP.toString(), value, this.action.getTtl());
            });
        }
    }
}
