package com.securenative.models;

import java.util.Map;

public class RequestOptions {
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
    private String timestamp;

    public RequestOptions(String hostId, String appName, String url, String method, String userAgent, Map<String, String> headers, String body, String ip, String remoteIp, String fp, String cid, String vid, String timestamp) {
        this.hostId = hostId;
        this.appName = appName;
        this.url = url;
        this.method = method;
        this.userAgent = userAgent;
        this.headers = headers;
        this.body = body;
        this.ip = ip;
        this.remoteIp = remoteIp;
        this.fp = fp;
        this.cid = cid;
        this.vid = vid;
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }
}
