package com.securenative.utils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.asynchttpclient.netty.channel.DefaultChannelPool;
import org.asynchttpclient.netty.handler.HttpHandler;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.LoggerFactory;

public class Logger {
    private static org.slf4j.Logger logger;

    public static org.slf4j.Logger getLogger() {
        return logger;
    }

    public static void initLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger(Logger.class);
            org.apache.log4j.Logger.getRootLogger().setLevel(Level.ERROR);
        }
    }

    public static void configureLogger() {
        BasicConfigurator.configure();
        org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
        org.apache.log4j.Logger.getLogger(DefaultChannelPool.class).setLevel(Level.WARN);
        org.apache.log4j.Logger.getLogger(HttpHandler.class).setLevel(Level.WARN);
        org.apache.log4j.Logger.getLogger(PlatformDependent.class).setLevel(Level.WARN);
        org.apache.log4j.Logger.getLogger(ResourceLeakDetector.class).setLevel(Level.WARN);
    }
}
