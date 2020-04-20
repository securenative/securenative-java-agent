package com.securenative.configurations;

import com.securenative.events.Event;
import com.securenative.events.EventManager;
import com.securenative.utils.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigurationUpdaterRunnable implements Runnable {
    private EventManager eventManager;
    private String requestUrl;
    private Event event;
    private Thread worker;
    private AtomicBoolean running = new AtomicBoolean(false);
    private String interval;

    public ConfigurationUpdaterRunnable(EventManager eventManager, String requestUrl, Event event, String interval) {
        this.eventManager = eventManager;
        this.requestUrl = requestUrl;
        this.event = event;
        this.interval = interval;
    }

    @Override
    public void run() {
        Logger.getLogger().debug("ConfigurationUpdate");
        running.set(true);
        while (running.get()) {
            try {
                Thread.sleep(Long.parseLong(this.interval));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.getLogger().debug("Thread was interrupted, Closing configuration updater");
            }
            // TODO run thread
            // TODO send event
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
