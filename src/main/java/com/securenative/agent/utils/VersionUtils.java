package com.securenative.agent.utils;

import com.securenative.agent.snpackage.PackageManager;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class VersionUtils {
    public static synchronized String getVersion() {
        String version = null;

        // try to load from maven properties first
        try {
            String filePath = "./pom.xml";
            Document document = PackageManager.readPackageFile(filePath);
            NodeList parent = document.getElementsByTagName("parent");

            if (parent.getLength() > 0) {
                version = PackageManager.parseParent(parent, "version");
            } else {
                version = document.getElementsByTagName("version").item(0).getTextContent();
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
