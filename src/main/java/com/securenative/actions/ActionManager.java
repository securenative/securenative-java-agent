package com.securenative.actions;

import com.securenative.models.ProcessesType;
import com.securenative.processors.ProcessorsFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.securenative.processors.ProcessorType.*;

public class ActionManager {
    private static final Map<String, String> actionProcessors = new HashMap<String, String>() {{
        put(ProcessesType.BLOCK_IP.getType(), BLACKLIST_IP.getType());
        put(ProcessesType.UNBLOCK_IP.getType(), WHITELIST_IP.getType());
        put(ProcessesType.ALLOW_IP.getType(), DELETE_BLACKLISTED.getType());
    }};

    public static void enforceActions(List<Action> actions) {
        actions.forEach(action -> {
            if (ActionManager.actionProcessors.get(action.getName()) != null) {
                ProcessorsFactory.getActionProcessor(ActionManager.actionProcessors.get(action.getName()), action).apply();
            }
        });
    }
}
