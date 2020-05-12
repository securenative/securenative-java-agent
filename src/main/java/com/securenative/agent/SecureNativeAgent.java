package com.securenative.agent;

import com.securenative.agent.config.SecureNativeOptions;
import com.securenative.agent.snpackage.PackageManager;
import com.securenative.agent.utils.Utils;
import com.securenative.agent.config.ConfigurationManager;
import com.securenative.agent.exceptions.SecureNativeSDKException;
import com.securenative.agent.module.ModuleManager;
import com.securenative.agent.snpackage.PackageItem;

import java.lang.instrument.Instrumentation;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class SecureNativeAgent {
    public static void premain(String args, Instrumentation inst) {
        try {
            // Set package information
            String PACKAGE_FILE_NAME = "/pom.xml";
            PackageItem appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));
            SecureNativeOptions config = ConfigurationManager.loadConfig();

            // Set default app name
            config.setAppName(appPkg.getName());
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            rootLogger.setLevel(Level.FINE); // TODO implement securenative logger

            System.out.println(String.format("Loaded SecureNative Agent Configurations %s", config.toString()));

            // Get relevant module
            ModuleManager moduleManager = new ModuleManager(appPkg);
            SecureNative secureNative = null;
            try {
                System.out.println("Starting SecureNative Java Agent");
                secureNative = new SecureNative(moduleManager, config);
            } catch (SecureNativeSDKException e) {
                System.err.println("Could not find SecureNative api key. aborting.");
                System.exit(1);
            }

            // Start agent
            System.out.println("Starting version compatibility check");

            if (Utils.versionCompare(System.getProperty("java.version"), config.getMinSupportedVersion()) < 0) {
                System.err.println(String.format("This version of Java %s isn't supported by SecureNative, minimum required version is %s", appPkg.getVersion(), config.getMinSupportedVersion()));
                System.err.println("Visit our docs to find out more: https://docs.securenative.com/docs/integrations/sdk/#java");
                System.exit(1);
            }

            SecureNative finalSecureNative = secureNative;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Received exit signal, exiting SecureNative agent..");
                finalSecureNative.stopAgent();
                System.exit(0);
            }));

            secureNative.startAgent();
            System.out.println("SecureNative agent was successfully started");
        } catch (Exception e) {
            System.out.println("Failed to start SecureNative Java Agent");
            System.out.println(e.getMessage());
        }
    }
}
