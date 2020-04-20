package com.securenative;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Schedualer {
    static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    public static void delayedExecutor(long delay, TimeUnit unit, Runnable r) {
        SCHEDULER.schedule(r, delay, unit);
    }
}
