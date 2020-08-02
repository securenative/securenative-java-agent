package com.securenative.agent.actions;

import com.securenative.agent.processors.ProcessorType;
import com.securenative.agent.processors.ProcessorsFactory;
import com.securenative.agent.enums.ProcessesType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManager {
    private static final Map<String, String> actionProcessors = new HashMap<String, String>() {{
        put(ProcessesType.BLOCK_IP.getType(), ProcessorType.BLACKLIST_IP.getType());
        put(ProcessesType.UNBLOCK_IP.getType(), ProcessorType.WHITELIST_IP.getType());
        put(ProcessesType.ALLOW_IP.getType(), ProcessorType.DELETE_BLACKLISTED.getType());
    }};

    public static void enforceActions(List<Action> actions) {
        actions.forEach(action -> {
            if (ActionManager.actionProcessors.get(action.getName()) != null) {
                ProcessorsFactory.getActionProcessor(ActionManager.actionProcessors.get(action.getName()), action).apply();
            }
        });
    }
}
