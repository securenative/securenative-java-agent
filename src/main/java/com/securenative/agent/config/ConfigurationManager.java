package com.securenative.agent.config;

import com.securenative.agent.ResourceStream;
import com.securenative.agent.ResourceStreamImpl;
import com.securenative.agent.enums.FailoverStrategy;
import com.securenative.agent.utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

public class ConfigurationManager {
    private static final String DEFAULT_CONFIG_FILE = System.getProperty("user.dir").concat("/securenative.properties");
    private static final String CUSTOM_CONFIG_FILE_ENV_NAME = "SECURENATIVE_CONFIG_FILE";
    private static ResourceStream resourceStream = new ResourceStreamImpl();

    private static String getEnvOrDefault(String envName, String defaultValue) {
        String envValue = System.getenv(envName);
        if (envValue != null) {
            return envValue;
        }

        String propValue = System.getProperty(envName);
        if (propValue != null) {
            return propValue;
        }
        return defaultValue;
    }

    public static void setResourceStream(ResourceStream resourceStream) {
        ConfigurationManager.resourceStream = resourceStream;
    }

    private static Properties loadProperties(Properties properties, InputStream inputStream) {
        try (final InputStream stream = inputStream) {
            properties.load(stream);
        } catch (Exception e) {
            return new Properties();
        }
        return properties;
    }

    private static Properties readResourceFile(String resourcePath) {
        Properties properties = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(resourcePath);
            properties.load(input);
        } catch (IOException ignore) {}

        return properties;
    }

    private static String getPropertyOrEnvOrDefault(Properties properties, String key, Object defaultValue) {
        String defaultStrValue = defaultValue == null ? null : defaultValue.toString();
        Object res = properties.getOrDefault(key, getEnvOrDefault(key, defaultStrValue));
        return res == null ? null : res.toString();
    }

    public static SecureNativeConfigurationBuilder configBuilder() {
        return SecureNativeConfigurationBuilder.defaultConfigBuilder();
    }

    public static SecureNativeOptions loadConfig() {
        String resourceFilePath = getEnvOrDefault(CUSTOM_CONFIG_FILE_ENV_NAME, DEFAULT_CONFIG_FILE);
        Properties properties = readResourceFile(resourceFilePath);
        return getOptions(properties);
    }

    public static SecureNativeOptions loadConfig(Path path) {
        Properties properties = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(path.toString());
            properties.load(input);
        } catch (IOException ignore) {

        }

        return getOptions(properties);
    }

    private static SecureNativeOptions getOptions(Properties properties) {
        SecureNativeConfigurationBuilder builder = SecureNativeConfigurationBuilder.defaultConfigBuilder();
        SecureNativeOptions defaultOptions = builder.build();

        builder.withApiKey(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_API_KEY", defaultOptions.getApiKey()))
                .withApiUrl(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_API_URL", defaultOptions.getApiUrl()))
                .withInterval(Utils.parseIntegerOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_INTERVAL", defaultOptions.getInterval()), defaultOptions.getInterval()))
                .withMaxEvents(Utils.parseIntegerOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_MAX_EVENTS", defaultOptions.getMaxEvents()), defaultOptions.getMaxEvents()))
                .withTimeout(Utils.parseIntegerOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_TIMEOUT", defaultOptions.getTimeout()), defaultOptions.getTimeout()))
                .withAutoSend(Utils.parseBooleanOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_AUTO_SEND", defaultOptions.getAutoSend()), defaultOptions.getAutoSend()))
                .withDisable(Utils.parseBooleanOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_DISABLE", defaultOptions.getDisabled()), defaultOptions.getDisabled()))
                .withLogLevel(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_LOG_LEVEL", defaultOptions.getLogLevel()))
                .withFailoverStrategy(FailoverStrategy.fromString(Objects.requireNonNull(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_FAILOVER_STRATEGY", defaultOptions.getFailoverStrategy())), defaultOptions.getFailoverStrategy()));

        return builder.build();
    }
}
