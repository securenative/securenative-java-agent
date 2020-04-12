package com.securenative.actions;

import com.securenative.models.ProcessesType;
import com.securenative.processors.BlacklistIp;
import com.securenative.processors.DeleteBlacklistedIp;
import com.securenative.processors.WhitelistIp;
import com.securenative.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManager {
    private static Map<String, Class<?>[]> actionProcessors = new HashMap<>();

    public ActionManager() {
        ActionManager.actionProcessors.put(ProcessesType.BLOCK_IP.getType(), new Class<?>[]{BlacklistIp.class});
        ActionManager.actionProcessors.put(ProcessesType.UNBLOCK_IP.getType(), new Class<?>[]{DeleteBlacklistedIp.class});
        ActionManager.actionProcessors.put(ProcessesType.ALLOW_IP.getType(), new Class<?>[]{WhitelistIp.class});
    }

    public static void enforceActions(List<Action> actions) {
        actions.forEach(action -> {
            if (ActionManager.actionProcessors.get(action.getName()) != null) {
                Class<?>[] processors = ActionManager.actionProcessors.get(action.getName());
                for (Class<?> processor : processors) {
                    try {
                        Class<?> pros = Class.forName(processor.getName());
                        Constructor<?> ctor = pros.getConstructor();
                        ctor.newInstance();
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        Logger.getLogger().debug(String.format("Could not initialize processor %s; %s", processor.getName(), e));
                    }
                }
            }
        });
    }
}
