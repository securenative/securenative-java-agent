package com.securenative;

import com.securenative.configurations.ConfigurationManager;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.module.ModuleManager;
import com.securenative.packagemanager.PackageManager;
import com.securenative.packagemanager.SnPackage;
import com.securenative.utils.Utils;

import java.lang.instrument.Instrumentation;

public class SecureNativeAgent {
    public static void premain(String args, Instrumentation inst) {
        try {
            // Set package information
            String PACKAGE_FILE_NAME = "/pom.xml";
            SnPackage appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));
            SecureNativeOptions config = ConfigurationManager.getConfig();

            // Set default app name
            config.setAppName(appPkg.getName());

            // Get relevant module
            ModuleManager moduleManager = new ModuleManager(appPkg);
            SecureNative secureNative = null;
            try {
                secureNative = new SecureNative(moduleManager, config);
            } catch (SecureNativeSDKException e) {
                Logger.getLogger().error("Could not find securenative api key. aborting.");
                System.exit(1);
            }

            // Init logger
            Logger.setLoggingEnable(true);
            Logger.getLogger().debug(String.format("Loaded Configurations %s", config.toString()));

            // Start agent
            Logger.getLogger().debug("Starting version compatibility check");

            if (Utils.versionCompare(System.getProperty("java.version"), config.getMinSupportedVersion()) > 0) {
                Logger.getLogger().error(String.format("This version of Java %s isn't supported by SecureNative, minimum required version is %s", appPkg.getVersion(), config.getMinSupportedVersion()));
                Logger.getLogger().error("Visit our docs to find out more: https://docs.securenative.com/docs/integrations/sdk/#java");
                System.exit(1);
            }

            SecureNative finalSecureNative = secureNative;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Logger.getLogger().debug("Received exit signal, exiting..");
                finalSecureNative.stopAgent();
                System.exit(0);
            }));

            secureNative.startAgent();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
