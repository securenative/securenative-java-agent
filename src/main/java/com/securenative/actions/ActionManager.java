package com.securenative.actions;

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
        // TODO [MATAN]: Avoid using hardcoded stings, use enums instead
        ActionManager.actionProcessors.put("block_ip", new Class<?>[]{BlacklistIp.class});
        ActionManager.actionProcessors.put("unblock_ip", new Class<?>[]{DeleteBlacklistedIp.class});
        ActionManager.actionProcessors.put("allow_ip", new Class<?>[]{WhitelistIp.class});
    }

    public static void enforceActions(List<Action> actions) {
        actions.forEach(action -> {
            if (ActionManager.actionProcessors.get(action.getName()) != null) {
                Class<?>[] processors = ActionManager.actionProcessors.get(action.getName());
                for (Class<?> processor : processors) {
                    try {
                        Class<?> pros = Class.forName(processor.getName());
                        Constructor<?> ctor = pros.getConstructor();
                        // TODO [MATAN]: if I got it right these are processors, your'e initializing the ctor,
                        // but there are processors with no empty constructor
                        ctor.newInstance();
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        Logger.getLogger().debug(String.format("Could not initialize processor %s; %s", processor.getName(), e));
                    }
                }
            }
        });
    }
}
