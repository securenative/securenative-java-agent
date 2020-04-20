package com.securenative.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    public static void delayedExecutor(long delay, TimeUnit unit, Runnable r) {
        SCHEDULER.schedule(r, delay, unit);
    }
}
