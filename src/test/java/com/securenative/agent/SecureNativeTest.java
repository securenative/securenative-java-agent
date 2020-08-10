package com.securenative.agent;

import com.securenative.agent.config.ConfigurationManager;
import com.securenative.agent.config.SecureNativeOptions;
import com.securenative.agent.exceptions.SecureNativeConfigException;
import com.securenative.agent.module.ModuleManager;
import com.securenative.agent.snpackage.PackageManager;
import com.securenative.agent.enums.FailoverStrategy;
import com.securenative.agent.exceptions.SecureNativeSDKException;
import com.securenative.agent.snpackage.PackageItem;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecureNativeTest {
    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(1)
    @DisplayName("Should init SDK with all public methods defined")
    public void initSDKWithPublicMethodsDefinedTest() {
        assertThat(SecureNative.class).hasDeclaredMethods("agentLogin", "agentLogout", "startAgent", "stopAgent");
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @SetSystemProperty(key = "SECURENATIVE_CONFIG_FILE", value = "FILE_THAT_MISSING")
    @Order(2)
    @DisplayName("Should throw when try init sdk without api key")
    public void initSDKWithoutApiKeyShouldThrowTest() throws SecureNativeConfigException, SecureNativeSDKException {
        String PACKAGE_FILE_NAME = "/pom.xml";
        PackageItem appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));
        SecureNativeOptions config = ConfigurationManager.loadConfig();
        ModuleManager moduleManager = new ModuleManager(appPkg);

        assertThrows(SecureNativeSDKException.class, () -> {
            SecureNative secureNative = new SecureNative(moduleManager, config);
        });
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(3)
    @DisplayName("Should init SDK with API key and default options")
    public void initSDKWithApiKeyAndDefaultsTest() throws SecureNativeConfigException, SecureNativeSDKException {
        final String apiKey = "API_KEY";
        String PACKAGE_FILE_NAME = "/pom.xml";

        PackageItem appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));
        SecureNativeOptions config = ConfigurationManager.loadConfig();
        config.setApiKey(apiKey);

        ModuleManager moduleManager = new ModuleManager(appPkg);
        SecureNative secureNative = new SecureNative(moduleManager, config);
        SecureNativeOptions options = secureNative.getOptions();

        assertThat(options.getApiKey()).isEqualTo(apiKey);
        assertThat(options.getApiUrl()).isEqualTo("https://api.securenative.com/collector/api/v1");
        assertThat(options.getInterval()).isEqualTo(1000);
        assertThat(options.getTimeout()).isEqualTo(2000);
        assertThat(options.getMaxEvents()).isEqualTo(1000);
        assertThat(options.getAutoSend()).isEqualTo(true);
        assertThat(options.getDisabled()).isEqualTo(false);
        assertThat(options.getLogLevel()).isEqualTo("debug");
        assertThat(options.getFailoverStrategy()).isEqualTo(FailoverStrategy.FAIL_OPEN);
    }
}
