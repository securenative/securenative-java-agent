package com.securenative;

import com.securenative.config.ConfigurationManager;
import com.securenative.config.SecureNativeOptions;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.module.ModuleManager;
import com.securenative.snpackage.PackageItem;
import com.securenative.snpackage.PackageManager;
import com.securenative.utils.Logger;
import com.securenative.utils.Utils;

import java.lang.instrument.Instrumentation;


public class SecureNativeAgent {
    public static void premain(String args, Instrumentation inst) {
        try {
            // Set package information
            String PACKAGE_FILE_NAME = "/pom.xml";
            PackageItem appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));
            SecureNativeOptions config = ConfigurationManager.loadConfig();

            // Set default app name
            config.setAppName(appPkg.getName());

            // Configure logger
            Logger.initLogger(config.getLogLevel().toLowerCase());
            final Logger logger = Logger.getLogger(SecureNative.class);
            logger.debug(String.format("Loaded Configurations %s", config.toString()));

            // Get relevant module
            ModuleManager moduleManager = new ModuleManager(appPkg);
            SecureNative secureNative = null;
            try {
                secureNative = new SecureNative(moduleManager, config);
            } catch (SecureNativeSDKException e) {
                logger.error("Could not find securenative api key. aborting.");
                System.err.println("Could not find securenative api key. aborting.");
                System.exit(1);
            }

            // Start agent
            logger.debug("Starting version compatibility check");

            if (Utils.versionCompare(System.getProperty("java.version"), config.getMinSupportedVersion()) < 0) {
                logger.error(String.format("This version of Java %s isn't supported by SecureNative, minimum required version is %s", appPkg.getVersion(), config.getMinSupportedVersion()));
                logger.error("Visit our docs to find out more: https://docs.securenative.com/docs/integrations/sdk/#java");
                System.exit(1);
            }

            SecureNative finalSecureNative = secureNative;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.debug("Received exit signal, exiting..");
                finalSecureNative.stopAgent();
                System.exit(0);
            }));

            secureNative.startAgent();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
