package com.securenative.module;

import com.securenative.models.Dependency;
import com.securenative.snpackage.PackageItem;

public class ModuleManager {
    public String framework;
    public String frameworkVersion;

    public ModuleManager(PackageItem packageItem) {
        for (Dependency d : packageItem.getDependencies()) {
            if (d.getName().toLowerCase().contains("spring")) {
                this.framework = d.getName();
                this.frameworkVersion = d.getVersion();
            }

            // Default framework
            if (this.framework == null) {
                this.framework = "spring";
            }
        }
    }

    public String getFramework() {
        return this.framework;
    }

    public String getFrameworkVersion() {
        return this.frameworkVersion;
    }
}
