package com.securenative.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.utils.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ConfigurationManager {
    static SecureNativeOptions config;
    static String CONFIG_FILE = "securenative.json";

    // For test purposes
    public ConfigurationManager(String configFile) {
        CONFIG_FILE = configFile;
    }

    public static SecureNativeOptions getConfig() {
        if (config == null) {
            SecureNativeOptions fileConfig = readConfigFile();
            config = new SecureNativeOptions();

            if (fileConfig != null && fileConfig.getApiUrl() != null) {
                config.setApiUrl(fileConfig.getApiUrl());
            } else {
                if (System.getenv("SECURENATIVE_API_URL") != null) {
                    config.setApiUrl(System.getenv("SECURENATIVE_API_URL"));
                } else {
                    config.setApiUrl("https://api.securenative.com/collector/api/v1");
                }
            }

            if (fileConfig != null && fileConfig.getApiKey() != null) {
                config.setApiKey(fileConfig.getApiKey());
            } else {
                if (System.getenv("SECURENATIVE_API_KEY") != null) {
                    config.setApiKey(System.getenv("SECURENATIVE_API_KEY"));
                } else {
                    config.setApiKey(null);
                }
            }

            if (fileConfig != null && fileConfig.getAppName() != null) {
                config.setAppName(fileConfig.getAppName());
            } else {
                if (System.getenv("SECURENATIVE_APP_NAME") != null) {
                    config.setAppName(System.getenv("SECURENATIVE_APP_NAME"));
                } else {
                    config.setAppName("");
                }
            }

            if (fileConfig != null && fileConfig.getInterval() != 0) {
                config.setInterval(fileConfig.getInterval());
            } else {
                if (System.getenv("SECURENATIVE_INTERVAL") != null) {
                    config.setInterval(Integer.parseInt(System.getenv("SECURENATIVE_INTERVAL")));
                } else {
                    config.setInterval(1000);
                }
            }

            if (fileConfig != null && fileConfig.getHeartbeatPeriod() != 0) {
                config.setHeartbeatPeriod(fileConfig.getHeartbeatPeriod());
            } else {
                if (System.getenv("SECURENATIVE_HEARTBEAT_PERIOD") != null) {
                    config.setHeartbeatPeriod(Integer.parseInt(System.getenv("SECURENATIVE_HEARTBEAT_PERIOD")));
                } else {
                    config.setHeartbeatPeriod(60 * 5 * 1000);
                }
            }

            if (fileConfig != null && fileConfig.getHeartbeatDelay() != 0) {
                config.setHeartbeatDelay(fileConfig.getHeartbeatDelay());
            } else {
                if (System.getenv("SECURENATIVE_HEARTBEAT_DELAY") != null) {
                    config.setHeartbeatDelay(Integer.parseInt(System.getenv("SECURENATIVE_HEARTBEAT_DELAY")));
                } else {
                    config.setHeartbeatDelay(0);
                }
            }

            if (fileConfig != null && fileConfig.getConfigUpdatePeriod() != 0) {
                config.setConfigUpdatePeriod(fileConfig.getConfigUpdatePeriod());
            } else {
                if (System.getenv("SECURENATIVE_CONFIG_PERIOD") != null) {
                    config.setConfigUpdatePeriod(Integer.parseInt(System.getenv("SECURENATIVE_CONFIG_PERIOD")));
                } else {
                    config.setConfigUpdatePeriod(60 * 15 * 1000);
                }
            }

            if (fileConfig != null && fileConfig.getConfigUpdateDelay() != 0) {
                config.setConfigUpdateDelay(fileConfig.getConfigUpdateDelay());
            } else {
                if (System.getenv("SECURENATIVE_CONFIG_DELAY") != null) {
                    config.setConfigUpdateDelay(Integer.parseInt(System.getenv("SECURENATIVE_CONFIG_DELAY")));
                } else {
                    config.setConfigUpdateDelay(0);
                }
            }

            if (fileConfig != null && fileConfig.getMaxEvents() != 0) {
                config.setMaxEvents(fileConfig.getMaxEvents());
            } else {
                if (System.getenv("SECURENATIVE_MAX_EVENTS") != null) {
                    config.setMaxEvents(Integer.parseInt(System.getenv("SECURENATIVE_MAX_EVENTS")));
                } else {
                    config.setMaxEvents(1000);
                }
            }

            if (fileConfig != null && fileConfig.getTimeout() != 0) {
                config.setTimeout(fileConfig.getTimeout());
            } else {
                if (System.getenv("SECURENATIVE_TIMEOUT") != null) {
                    config.setTimeout(Integer.parseInt(System.getenv("SECURENATIVE_TIMEOUT")));
                } else {
                    config.setTimeout(1500);
                }
            }

            if (fileConfig != null) {
                config.setAutoSend(fileConfig.getAutoSend());
            } else {
                if (System.getenv("SECURENATIVE_AUTO_SEND") != null) {
                    config.setAutoSend(Boolean.parseBoolean(System.getenv("SECURENATIVE_AUTO_SEND")));
                } else {
                    config.setAutoSend(true);
                }
            }

            if (fileConfig != null) {
                config.setAgentDisable(fileConfig.getAgentDisable());
            } else {
                if (System.getenv("SECURENATIVE_DISABLE") != null) {
                    config.setAgentDisable(Boolean.parseBoolean(System.getenv("SECURENATIVE_DISABLE")));
                } else {
                    config.setAgentDisable(false);
                }
            }

            if (fileConfig != null) {
                config.setLoggingEnabled(fileConfig.isLoggingEnabled());
            } else {
                if (System.getenv("SECURENATIVE_LOGGING_ENABLED") != null) {
                    config.setLoggingEnabled(Boolean.parseBoolean(System.getenv("SECURENATIVE_LOGGING_ENABLED")));
                } else {
                    config.setLoggingEnabled(true);
                }
            }

            if (fileConfig != null) {
                config.setDebugMode(fileConfig.getDebugMode());
            } else {
                if (System.getenv("SECURENATIVE_DEBUG_MODE") != null) {
                    config.setDebugMode(Boolean.parseBoolean(System.getenv("SECURENATIVE_DEBUG_MODE")));
                } else {
                    config.setDebugMode(false);
                }
            }
            config.setMinSupportedVersion("8");
        }

        return config;
    }

    public static SecureNativeOptions readConfigFile() {
        Logger.getLogger().debug(String.format("Reading %s", CONFIG_FILE));

        String configPath = System.getProperty("user.dir") + "/" + CONFIG_FILE;
        Path path = Paths.get(configPath);

        if (Files.exists(path)) {
            String content;
            try {
                content = new String(Files.readAllBytes(path));
            } catch (IOException e) {
                Logger.getLogger().debug(String.format("Unable to parse %s", CONFIG_FILE));
                return null;
            }

            SecureNativeOptions conf;
            ObjectMapper mapper = new ObjectMapper();
            try {
                conf = mapper.readValue(content, SecureNativeOptions.class);
            } catch (IOException e) {
                Logger.getLogger().debug(String.format("Unable to parse %s", CONFIG_FILE));
                return null;
            }
            return conf;
        }

        return null;
    }
}
