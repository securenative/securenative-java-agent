package com.securenative.config;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ExecuteManager {
    private static final Logger logger = Logger.getLogger(ExecuteManager.class.getName());
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
            logger.fine(String.format("Executing task %s", this.name));
            this.execute.scheduleAtFixedRate(this.task, this.delay, this.period, TimeUnit.MILLISECONDS);
        } catch (IllegalArgumentException | NullPointerException | RejectedExecutionException e) {
            logger.fine(String.format("Could not start executing task %s; %s", this.task.toString(), e));
        }
    }

    public void shutdown() {
        try {
            this.execute.shutdown();
        } catch (SecurityException e) {
            logger.fine(String.format("Could not shutdown task; %s", e));
        }
    }
}
