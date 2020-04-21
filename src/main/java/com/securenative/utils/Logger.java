package com.securenative.utils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.slf4j.LoggerFactory;

public class Logger {
    private static org.slf4j.Logger logger = new ImpotentLogger();

    public static org.slf4j.Logger getLogger() {
        return logger;
    }

    public static void setLoggingEnable(boolean isLoggingEnabledInput) {
        BasicConfigurator.configure();
        org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
        if (isLoggingEnabledInput) {
            logger = LoggerFactory.getLogger(Logger.class);
            logger.debug("SecureNative logger is enabled");
            return;
        }
        logger = new ImpotentLogger();
    }
}
