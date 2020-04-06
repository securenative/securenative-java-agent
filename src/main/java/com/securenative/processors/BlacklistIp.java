package com.securenative.processors;

import com.securenative.Logger;
import com.securenative.actions.Action;
import com.securenative.actions.ActionList;
import com.securenative.models.SetType;

public class BlacklistIp {
    private Action action;

    public BlacklistIp(Action action) {
        this.action = action;
    }

    // TODO [MATAN]: Consider using an interface for all processor
    // then you can have Class<Processor> instead of Class<?> and ensure type-safety
    public void apply() {
        // TODO [MATAN]: how changes to the action are propagated to this class
        if (this.action.getValues().size() > 0) {
            this.action.getValues().forEach(value -> {
                Logger.getLogger().debug(String.format("Blacklisting ip: %s", value));
                ActionList.blackList.add(SetType.IP.toString(), value, this.action.ttl);
            });
        }
    }
}
