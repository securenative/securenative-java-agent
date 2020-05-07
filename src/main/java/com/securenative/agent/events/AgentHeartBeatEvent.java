package com.securenative.agent.events;

import com.securenative.agent.utils.Utils;
import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.snpackage.PackageItem;
import com.securenative.agent.snpackage.PackageManager;
import com.securenative.agent.utils.DateUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class AgentHeartBeatEvent implements Event {
    private static final Logger logger = Logger.getLogger(AgentHeartBeatEvent.class.getName());
    private static final String PACKAGE_FILE_NAME = "/pom.xml";
    private final String eventType;
    private String runtime;
    private String hostId;
    private String hostname;
    private String agentVersion;
    private String timestamp;
    private static PackageItem agentPackage = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));

    public AgentHeartBeatEvent() {
        this.eventType = EventTypes.HEARTBEAT.getType();
        this.timestamp = DateUtils.generateTimestamp();
        try {
            this.hostname = InetAddress.getLocalHost().getHostName();
            this.hostId = Utils.calculateHash(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            logger.fine(String.format("Could not find host name; %s", e));
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

    public static void setAgentPackage(PackageItem agentPackage) {
        AgentHeartBeatEvent.agentPackage = agentPackage;
    }
}
