package com.securenative.processors;

import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.enums.SetType;

import java.util.logging.Logger;

public class BlacklistIp extends Processor {
    private static final Logger logger = Logger.getLogger(BlacklistIp.class.getName());
    private Action action;

    public BlacklistIp(Action action) {
        this.action = action;
    }

    @Override
    public void apply() {
        if (this.action.getValues().size() > 0) {
            this.action.getValues().forEach(value -> {
                logger.fine(String.format("Blacklisting ip: %s", value));
                ActionList.blackList.add(SetType.IP.toString(), value, this.action.getTtl());
            });
        }
    }
}
