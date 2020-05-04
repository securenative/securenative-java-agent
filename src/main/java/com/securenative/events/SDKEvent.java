package com.securenative.events;

import com.securenative.configurations.SecureNativeOptions;
import com.securenative.models.EventOptions;
import com.securenative.models.EventTypes;
import com.securenative.models.User;
import com.securenative.utils.Logger;
import com.securenative.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletRequest;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

public class SDKEvent implements Event {
    private static final Logger logger = Logger.getLogger(SDKEvent.class);
    private final String eventType;
    private String cid;
    private String vid;
    private String fp;
    private String ip;
    private String remoteIp;
    private String userAgent;
    private User user;
    private String timestamp;
    private String device;
    private Map<String, String> params;

    public SDKEvent(ServletRequest request, EventOptions eventOptions, SecureNativeOptions snOptions) {
        logger.debug("Building new SDK event");
        if (eventOptions.getEventType() != null) {
            this.eventType = eventOptions.getEventType();
        } else {
            this.eventType = EventTypes.SDK.getType();
        }

        String cookie = "{}";
        if (!Utils.cookieIdFromRequest(request, snOptions).equals("")) {
            cookie = Utils.cookieIdFromRequest(request, snOptions);
        } else if (!Utils.secureHeaderFromRequest(request).equals("")) {
            cookie = Utils.secureHeaderFromRequest(request);
        }
        logger.debug(String.format("Cookie from request; %s", cookie));

        String cookieDecoded = "{}";
        try {
            cookieDecoded = Utils.decrypt(cookie, snOptions.getApiKey());
            logger.debug(String.format("Cookie decoded; %s", cookieDecoded));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | NumberFormatException e) {
            logger.debug("Could not decode cookie; %s", e);
        }

        try {
            JSONObject clientFP = new JSONObject(cookieDecoded);
            logger.debug(String.format("Extracted user FP; %s", clientFP));
            this.cid = clientFP.getString("cid");
            this.fp = clientFP.getString("fp");
        } catch (JSONException e) {
            logger.debug("Could not decode json object; %s", e);
            this.cid = "";
            this.fp = "";
        }

        if (eventOptions.getIp() != null && !eventOptions.getIp().equals("")) {
            this.ip = eventOptions.getIp();
        } else if (request != null) {
            this.ip = Utils.clientIpFromRequest(request);
        }

        if (eventOptions.getRemoteIp() != null && !eventOptions.getRemoteIp().equals("")) {
            this.remoteIp = eventOptions.getRemoteIp();
        } else if (request != null) {
            this.remoteIp = Utils.remoteIpFromRequest(request);
        }

        if (eventOptions.getUserAgent() != null && !eventOptions.getUserAgent().equals("")) {
            this.userAgent = eventOptions.getUserAgent();
        } else if (request != null) {
            this.userAgent = Utils.userAgentFromRequest(request);
        }

        this.vid = UUID.randomUUID().toString();
        this.user = eventOptions.getUser();
        this.timestamp = Utils.generateTimestamp();
        this.device = eventOptions.getDevice();
        this.params = eventOptions.getParams();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getCid() {
        return cid;
    }

    public String getVid() {
        return vid;
    }

    public String getFp() {
        return fp;
    }

    public String getIp() {
        return ip;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public User getUser() {
        return user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDevice() {
        return device;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
