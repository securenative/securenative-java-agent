package com.securenative.module;

import com.securenative.models.Dependency;
import com.securenative.packagemanager.SnPackage;

public class ModuleManager {
    public String framework;
    public String frameworkVersion;

    public ModuleManager(SnPackage snPackage) {
        for (Dependency d : snPackage.getDependencies()) {
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
