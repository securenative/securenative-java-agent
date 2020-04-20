package com.securenative.events;

import com.securenative.Logger;
import com.securenative.models.EventTypes;
import com.securenative.snpackage.PackageManager;
import com.securenative.snpackage.PackageItem;
import com.securenative.utils.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

public class AgentHeartBeatEvent implements Event {
    private static final String PACKAGE_FILE_NAME = "/pom.xml";
    private final String eventType;
    private String runtime;
    private String hostId;
    private String hostname;
    private String agentVersion;
    private long ts;
    private static PackageItem agentPackage = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));

    public AgentHeartBeatEvent() {
        this.eventType = EventTypes.HEARTBEAT.getType();
        this.ts = ZonedDateTime.now().toEpochSecond();
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

    public long getTs() {
        return ts;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public static void setAgentPackage(PackageItem agentPackage) {
        AgentHeartBeatEvent.agentPackage = agentPackage;
    }
}
