package com.securenative.agent.actions;

import com.securenative.agent.enums.ActionListType;

public class ActionList {
    public static ActionSet whitelist = new ActionSet(ActionListType.WHITELIST.getType());
    public static ActionSet blackList = new ActionSet(ActionListType.BLACKLIST.getType());
}
