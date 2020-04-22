package com.securenative.configurations;

import com.securenative.utils.Logger;

import java.util.concurrent.*;

public class ExecuteManager {
    private final ScheduledExecutorService execute;
    private final long delay;
    private final long period;
    private final Runnable task;
    private final String name;

    public ExecuteManager(long delay, long period, String name, Runnable task) {
        this.execute = Executors.newSingleThreadScheduledExecutor();
        this.delay = delay;
        this.period = period;
        this.task = task;
        this.name = name;
    }

    public void execute() {
        try {
            Logger.getLogger().debug(String.format("Executing task %s", this.name));
            this.execute.scheduleAtFixedRate(this.task, this.delay, this.period, TimeUnit.MILLISECONDS);
        } catch (IllegalArgumentException | NullPointerException | RejectedExecutionException e) {
            Logger.getLogger().debug(String.format("Could not start executing task %s; %s", this.task.toString(), e));
        }
    }

    public void shutdown() {
        try {
            this.execute.shutdown();
        } catch (SecurityException e) {
            Logger.getLogger().debug("Could not shutdown task; %s", e);
        }
    }
}
