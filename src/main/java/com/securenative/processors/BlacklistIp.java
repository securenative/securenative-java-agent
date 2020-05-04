package com.securenative.processors;

import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.models.SetType;
import com.securenative.utils.Logger;

public class BlacklistIp extends Processor {
    private static final Logger logger = Logger.getLogger(BlacklistIp.class);
    private Action action;

    public BlacklistIp(Action action) {
        this.action = action;
    }

    @Override
    public void apply() {
        if (this.action.getValues().size() > 0) {
            this.action.getValues().forEach(value -> {
                logger.debug(String.format("Blacklisting ip: %s", value));
                ActionList.blackList.add(SetType.IP.toString(), value, this.action.getTtl());
            });
        }
    }
}
