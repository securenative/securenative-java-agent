package com.securenative.agent;

import com.securenative.agent.config.ConfigurationManager;
import com.securenative.agent.config.SecureNativeOptions;
import com.securenative.agent.snpackage.PackageItem;
import com.securenative.agent.snpackage.PackageManager;
import org.junit.Test;

public class PackageManagerTest {
    @Test
    public void testPackageManager() {
        PackageItem appPkg = PackageManager.getPackage("/Users/inbaltako/code/securenative-java-agent/src/test/resources/pom.xml");
        SecureNativeOptions config = ConfigurationManager.loadConfig();
    }
}
