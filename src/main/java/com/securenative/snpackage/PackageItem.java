package com.securenative.snpackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.models.Dependency;

public class PackageItem {
    @JsonProperty("name") protected String name;
    @JsonProperty("version") protected String version;
    @JsonProperty("dependencies") protected Dependency[] dependencies;
    @JsonProperty("dependenciesHash") protected String dependenciesHash;

    public PackageItem(String name, String version, Dependency[] dependencies, String dependenciesHash) {
        this.name = name;
        this.version = version;
        this.dependencies = dependencies;
        this.dependenciesHash = dependenciesHash;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Dependency[] getDependencies() {
        return dependencies;
    }

    public String getDependenciesHash() {
        return dependenciesHash;
    }

    public Dependency getDependency(int index) {
        return this.dependencies[index];
    }
}
