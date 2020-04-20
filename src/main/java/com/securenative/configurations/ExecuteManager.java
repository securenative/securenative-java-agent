package com.securenative.configurations;

import com.securenative.utils.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecuteManager {
    private final ScheduledExecutorService execute;
    private final long delay;
    private final long period;
    private final Runnable task;

    public ExecuteManager(long delay, long period, Runnable task) {
        this.execute = Executors.newSingleThreadScheduledExecutor();
        this.delay = delay;
        this.period = period;
        this.task = task;
    }

    public void execute() {
        try {
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
