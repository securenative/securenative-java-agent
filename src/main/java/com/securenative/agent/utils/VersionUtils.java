package com.securenative.agent.utils;

import com.securenative.agent.ResourceStream;
import com.securenative.agent.ResourceStreamImpl;

import java.io.InputStream;
import java.util.Properties;

public class VersionUtils {
    private static final ResourceStream resourceStream = new ResourceStreamImpl();

    public static synchronized String getVersion() {
        String version = null;

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            InputStream is = resourceStream.getInputStream("/META-INF/maven/com.securenative.java/securenative-java-agent/pom.properties");
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        }

        // fallback to using Java API
        if (version == null) {
            Package aPackage = VersionUtils.class.getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }

        if (version == null) {
            // we could not compute the version so use a blank
            version = "unknown";
        }

        return version;
    }
}
