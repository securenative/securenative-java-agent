package com.securenative.agent.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.utils.Utils;
import com.securenative.agent.snpackage.PackageItem;
import com.securenative.agent.snpackage.PackageManager;
import com.securenative.agent.utils.DateUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.logging.Logger;

public class AgentLoginEvent implements Event {
    private static final String PACKAGE_FILE_NAME = "pom.xml";
    private static final Logger logger = Logger.getLogger(AgentLoginEvent.class.getName());

    @JsonProperty("eventType")
    private final String eventType;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("package")
    private PackageItem packageItem;
    @JsonProperty("appName")
    private String appName;
    @JsonProperty("process")
    private SnProcess process;
    @JsonProperty("runtime")
    private SnRuntime snRuntime;
    @JsonProperty("os")
    private Os os;
    @JsonProperty("framework")
    private Framework framework;
    @JsonProperty("agent")
    private Agent agent;

    public AgentLoginEvent(String framework, String frameworkVersion, String appName) {
        String cwd = System.getProperty("user.dir");

        PackageItem appPkg = PackageManager.getPackage(String.join(cwd, PACKAGE_FILE_NAME));
        PackageItem agentPkg = PackageManager.getPackage(String.join("/sdk-base/", PACKAGE_FILE_NAME));

        this.appName = appName;
        this.framework = new Framework(framework, frameworkVersion);

        this.packageItem = new PackageItem(appPkg.getName(), appPkg.getVersion(), appPkg.getDependencies(), appPkg.getDependenciesHash());

        this.eventType = EventTypes.AGENT_LOG_IN.getType();

        this.snRuntime = new SnRuntime("java", System.getProperty("java.version"));

        AbstractMap.SimpleEntry<Long, String> processInfo = Utils.getProcessInfo();
        this.process = new SnProcess(
                processInfo.getKey(),
                processInfo.getValue(),
                System.getProperty("user.dir")
        );

        String hostId = null;
        String hostname = null;
        try {
            hostId = InetAddress.getLocalHost().getHostAddress();
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.fine(String.join("Could not find hostname and/or host address; ", e.toString()));
        }
        this.os = new Os(hostId, hostname, System.getProperty("os.arch"), System.getProperty("os.name"), Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().totalMemory());

        this.agent = new Agent("Server Agent", agentPkg.getVersion(), System.getProperty("java.class.path"));

        this.timestamp = DateUtils.generateTimestamp();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public PackageItem getPackageItem() {
        return packageItem;
    }

    public String getAppName() {
        return appName;
    }

    public SnProcess getProcess() {
        return process;
    }

    public SnRuntime getSnRuntime() {
        return snRuntime;
    }

    public Os getOs() {
        return os;
    }

    public Framework getFramework() {
        return framework;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setPackageItem(PackageItem packageItem) {
        this.packageItem = packageItem;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setProcess(SnProcess process) {
        this.process = process;
    }

    public void setSnRuntime(SnRuntime snRuntime) {
        this.snRuntime = snRuntime;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}

class SnProcess {
    @JsonProperty("pid") Long pid;
    @JsonProperty("name") String name;
    @JsonProperty("cwd") String cwd;

    public SnProcess(Long pid, String name, String cwd) {
        this.pid = pid;
        this.name = name;
        this.cwd = cwd;
    }
}

class SnRuntime {
    @JsonProperty("type") String type;
    @JsonProperty("version") String version;

    public SnRuntime(String type, String version) {
        this.type = type;
        this.version = version;
    }
}

class Os {
    @JsonProperty("hostId") String hostId;
    @JsonProperty("hostName") String hostname;
    @JsonProperty("arch") String arch;
    @JsonProperty("platform") String platform;
    @JsonProperty("cpus") Integer cpus;
    @JsonProperty("totalMemory") Long totalMemory;

    public Os(String hostId, String hostname, String arch, String platform, Integer cpus, Long totalMemory) {
        this.hostId = hostId;
        this.hostname = hostname;
        this.arch = arch;
        this.platform = platform;
        this.cpus = cpus;
        this.totalMemory = totalMemory;
    }
}

class Framework {
    @JsonProperty("type") String type;
    @JsonProperty("version") String version;

    public Framework(String type, String version) {
        this.type = type;
        this.version = version;
    }
}

class Agent {
    @JsonProperty("type") String type;
    @JsonProperty("version") String version;
    @JsonProperty("path") String path;

    public Agent(String type, String version, String path) {
        this.type = type;
        this.version = version;
        this.path = path;
    }
}