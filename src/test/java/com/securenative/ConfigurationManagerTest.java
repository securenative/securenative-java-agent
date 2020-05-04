package com.securenative;

import com.securenative.configurations.ConfigurationManager;
import com.securenative.configurations.SecureNativeOptions;
import org.junit.Assert;
import org.junit.Test;

public class ConfigurationManagerTest {
    @Test
    public void getConfiguration() {
        ConfigurationManager configurationManager = new ConfigurationManager("securenative-test.json");
        SecureNativeOptions options = configurationManager.getConfig();

        Assert.assertNotNull(options);
        Assert.assertEquals("http://www.test-agent-sn.com", options.getApiUrl());
        Assert.assertEquals(true, options.getDebugMode());
        Assert.assertEquals(options.getTimeout(), 1L);
    }

    @Test
    public void readConfigurationFile() {
        ConfigurationManager configurationManager = new ConfigurationManager("securenative-test.json");
        SecureNativeOptions options = configurationManager.readConfigFile();

        Assert.assertNotNull(options);
        Assert.assertEquals("http://www.test-agent-sn.com", options.getApiUrl());
        Assert.assertEquals(true, options.getDebugMode());
        Assert.assertNull(options.getAppName());
    }
}
