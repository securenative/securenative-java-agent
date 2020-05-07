package com.securenative.agent.snpackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.agent.models.Dependency;

public class PackageItem {
    @JsonProperty("name")
    private String name;
    @JsonProperty("version")
    private String version;
    @JsonProperty("dependencies")
    private Dependency[] dependencies;
    @JsonProperty("dependenciesHash")
    private String dependenciesHash;

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
