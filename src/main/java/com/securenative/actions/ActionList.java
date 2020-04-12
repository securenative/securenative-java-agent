package com.securenative.actions;

import com.securenative.models.ActionListType;

public class ActionList {
    public static ActionSet whitelist = new ActionSet(ActionListType.WHITELIST.getType());
    public static ActionSet blackList = new ActionSet(ActionListType.BLACKLIST.getType());
}
