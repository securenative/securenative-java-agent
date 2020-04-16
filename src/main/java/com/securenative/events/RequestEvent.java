package com.securenative.events;

import com.securenative.models.EventTypes;
import com.securenative.models.RequestOptions;

import java.util.Map;

public class RequestEvent implements Event {
    private String eventType;
    private String hostId;
    private String appName;
    private String url;
    private String method;
    private String userAgent;
    private Map<String, String> headers;
    private String body;
    private String ip;
    private String remoteIp;
    private String fp;
    private String cid;
    private String vid;
    private long ts;

    public RequestEvent(RequestOptions options) {
        this.eventType = EventTypes.REQUEST.getType();
        this.hostId = options.getHostId();
        this.appName = options.getAppName();
        this.url = options.getUrl();
        this.method = options.getMethod();
        this.userAgent = options.getUserAgent();
        this.headers = options.getHeaders();
        this.body = options.getBody();
        this.ip = options.getIp();
        this.remoteIp = options.getRemoteIp();
        this.fp = options.getFp();
        this.cid = options.getCid();
        this.vid = options.getVid();
        this.ts = options.getTs();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getHostId() {
        return hostId;
    }

    public String getAppName() {
        return appName;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getIp() {
        return ip;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public String getFp() {
        return fp;
    }

    public String getCid() {
        return cid;
    }

    public String getVid() {
        return vid;
    }

    public long getTs() {
        return ts;
    }
}
