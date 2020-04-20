package com.securenative.events;

import com.securenative.Logger;
import com.securenative.models.EventTypes;
import com.securenative.packagemanager.PackageManager;
import com.securenative.packagemanager.SnPackage;
import com.securenative.utils.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

public class AgentHeartBeatEvent implements Event {
    private static final String PACKAGE_FILE_NAME = "/pom.xml";
    private String eventType;
    private String runtime;
    private String hostId;
    private String hostname;
    private String agentVersion;
    private String timestamp;
    private static SnPackage agentPackage = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));

    public AgentHeartBeatEvent() {
        this.eventType = EventTypes.HEARTBEAT.getType();
        this.timestamp = Utils.generateTimestamp();
        try {
            this.hostname = InetAddress.getLocalHost().getHostName();
            this.hostId = Utils.calculateHash(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            Logger.getLogger().error("Could not find host name; ", e);
        }
        this.agentVersion = AgentHeartBeatEvent.agentPackage.getVersion();
        this.runtime = "java";
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getHostId() {
        return hostId;
    }

    public String getHostname() {
        return hostname;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
