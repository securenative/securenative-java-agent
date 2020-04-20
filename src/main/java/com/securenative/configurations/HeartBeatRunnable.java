package com.securenative.configurations;

import com.securenative.events.Event;
import com.securenative.utils.Logger;
import com.securenative.events.EventManager;

import java.util.concurrent.atomic.AtomicBoolean;

public class HeartBeatRunnable implements Runnable {
    private EventManager eventManager;
    private String requestUrl;
    private Event event;
    private long interval;
    private Thread worker;
    private AtomicBoolean running = new AtomicBoolean(false);

    public HeartBeatRunnable(EventManager eventManager, String requestUrl, Event event, long interval) {
        this.eventManager = eventManager;
        this.requestUrl = requestUrl;
        this.event = event;
        this.interval = interval;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.getLogger().debug("Thread was interrupted, Closing HeartBeat thread");
            }
            Logger.getLogger().debug("HeartBeat");
            this.eventManager.sendAsync(event, requestUrl);
        }
    }

    public void interrupt() {
        running.set(false);
        worker.interrupt();
    }

    public boolean isRunning() {
        return running.get();
    }
}
