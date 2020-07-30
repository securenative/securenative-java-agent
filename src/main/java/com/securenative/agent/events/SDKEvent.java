package com.securenative.agent.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.agent.SecureNative;
import com.securenative.agent.config.SecureNativeOptions;
import com.securenative.agent.context.SecureNativeContext;
import com.securenative.agent.context.SecureNativeContextBuilder;
import com.securenative.agent.models.ClientToken;
import com.securenative.agent.models.EventOptions;
import com.securenative.agent.models.RequestContext;
import com.securenative.agent.models.UserTraits;
import com.securenative.agent.utils.DateUtils;
import com.securenative.agent.utils.EncryptionUtils;

import java.util.*;
import java.util.logging.Logger;

public class SDKEvent implements Event {
    private final String rid;
    public String eventType;
    public String userId;
    private final UserTraits userTraits;
    public RequestContext request;
    public String timestamp;
    public Map<Object, Object> properties;
    public static final Logger logger = Logger.getLogger(SecureNative.class.getName());

    public SDKEvent(EventOptions event, SecureNativeOptions options) {
        SecureNativeContext context = event.getContext() != null?  event.getContext() : SecureNativeContextBuilder.defaultContextBuilder().build();

        ClientToken clientToken = decryptToken(context.getClientToken(), options.getApiKey());

        this.rid = UUID.randomUUID().toString();
        this.eventType = event.getEvent();
        this.userId = event.getUserId();
        this.userTraits = event.getUserTraits();
        this.request =  new RequestContext.RequestContextBuilder()
                .withCid(clientToken.getCid())
                .withVid(clientToken.getVid())
                .withFp(clientToken.getFp())
                .withIp(context.getIp())
                .withRemoteIp(context.getRemoteIp())
                .withMethod(context.getMethod())
                .withUrl(context.getUrl())
                .witHeaders(context.getHeaders())
                .build();
        this.timestamp = DateUtils.toTimestamp(event.getTimestamp());
        this.properties = event.getProperties();
    }

    private ClientToken decryptToken(String token, String key) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String decryptedClientToken = EncryptionUtils.decrypt(token, key);
            return mapper.readValue(decryptedClientToken, ClientToken.class);
        } catch (Exception ex) {
            logger.severe("Failed to decrypt token");
        }

        return new ClientToken();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getRid() {
        return rid;
    }

    public String getUserId() {
        return userId;
    }

    public UserTraits getUserTraits() {
        return userTraits;
    }

    public RequestContext getRequest() {
        return request;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Map<Object, Object> getProperties() {
        return properties;
    }
}