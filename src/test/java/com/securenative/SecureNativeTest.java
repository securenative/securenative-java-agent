package com.securenative;

import com.securenative.configurations.ConfigurationManager;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.module.ModuleManager;
import com.securenative.packagemanager.PackageManager;
import com.securenative.packagemanager.SnPackage;
import com.securenative.utils.Utils;
import org.junit.Assert;
import org.junit.Test;

public class SecureNativeTest {
    @Test(expected = SecureNativeSDKException.class)
    public void invalidInitialization() throws SecureNativeSDKException {
        SecureNativeOptions config = ConfigurationManager.getConfig();
        String PACKAGE_FILE_NAME = "/pom.xml";
        SnPackage appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));
        ModuleManager moduleManager = new ModuleManager(appPkg);

        new SecureNative(moduleManager, config);
    }

    @Test
    public void validateMinSupportedJavaVersion() {
        SecureNativeOptions config = ConfigurationManager.getConfig();
        Assert.assertTrue(Utils.versionCompare(System.getProperty("java.version"), config.getMinSupportedVersion()) > 0);
    }

    @Test
    public void readConfigurationFile() {
        String PACKAGE_FILE_NAME = "/pom.xml";
        SnPackage appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));

        String packageName = "com.securenative.java:securenative-java-agent";

        Assert.assertNotNull(appPkg);
        Assert.assertNotNull(appPkg.getDependenciesHash());
        Assert.assertEquals(appPkg.getName(),packageName);
        Assert.assertTrue(appPkg.getDependencies().length > 0);
    }
}
