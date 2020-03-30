package com.securenative.module;

import com.securenative.packagemanager.SnPackage;

public class ModuleManager {
    public String framework;
    public String frameworkVersion;

    public ModuleManager(SnPackage snPackage) {
        for (int i = 0; i < snPackage.getDependencies().length; i++) {
            if (snPackage.getDependency(i).getName().toLowerCase().contains("spring")) {
                this.framework = snPackage.getDependency(i).getName();
                this.frameworkVersion = snPackage.getDependency(i).getVersion();
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
