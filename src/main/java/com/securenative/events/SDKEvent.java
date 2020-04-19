package com.securenative.events;

import com.securenative.Logger;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.models.EventOptions;
import com.securenative.models.EventTypes;
import com.securenative.models.User;
import com.securenative.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

public class SDKEvent implements Event {
    private String eventType;
    private String cid;
    private String vid;
    private String fp;
    private String ip;
    private String remoteIP;
    private String userAgent;
    private User user;
    private long ts;
    private String device;
    private Map<String, String> params;

    public SDKEvent(ServletRequest request, EventOptions eventOptions, SecureNativeOptions snOptions) {
        this.eventType = EventTypes.SDK.getType();
        Logger.getLogger().debug("Building new SDK event");

        String cookie = "{}";
        if (!Utils.cookieIdFromRequest(request, snOptions).equals("")) {
            cookie = Utils.cookieIdFromRequest(request, snOptions);
        } else if (!Utils.secureHeaderFromRequest(request).equals("")) {
            cookie = Utils.secureHeaderFromRequest(request);
        }
        Logger.getLogger().debug(String.format("Cookie from request; %s", cookie));

        String cookieDecoded = "{}";
        try {
            cookieDecoded = Utils.decrypt(cookie, snOptions.getApiKey());
            Logger.getLogger().debug(String.format("Cookie decoded; %s", cookieDecoded));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            Logger.getLogger().error("Could not decode cookie; %s", e);
        }

        try {
            JSONObject clientFP = new JSONObject(cookieDecoded);
            Logger.getLogger().debug(String.format("Extracted user FP; %s", clientFP));
            this.cid = clientFP.getString("cid");
            this.fp = clientFP.getString("fp");
        } catch (JSONException e) {
            Logger.getLogger().error("Could not decode json object; %s", e);
        }

        if (eventOptions.getEventType() != null) {
            this.eventType = eventOptions.getEventType();
        } else {
            this.eventType = EventTypes.LOG_IN.getType();
        }

        if (eventOptions.getIp() != null && !eventOptions.getIp().equals("")) {
            this.ip = eventOptions.getIp();
        } else {
            this.ip = Utils.clientIpFromRequest(request);
        }

        if (eventOptions.getRemoteIp() != null && !eventOptions.getRemoteIp().equals("")) {
            this.remoteIP = eventOptions.getRemoteIp();
        } else {
            this.remoteIP = Utils.remoteIpFromRequest(request);
        }

        if (eventOptions.getUserAgent() != null && !eventOptions.getUserAgent().equals("")) {
            this.userAgent = eventOptions.getUserAgent();
        } else {
            this.userAgent = Utils.userAgentFromRequest(request);
        }

        this.vid = UUID.randomUUID().toString();
        this.user = eventOptions.getUser();
        this.ts = ZonedDateTime.now().toEpochSecond();
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

    public String getRemoteIP() {
        return remoteIP;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public User getUser() {
        return user;
    }

    public long getTs() {
        return ts;
    }

    public String getDevice() {
        return device;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
