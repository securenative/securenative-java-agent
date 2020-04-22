package com.securenative.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.configurations.AgentConfigOptions;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.ActionType;
import com.securenative.models.Message;
import com.securenative.models.RiskLevel;
import com.securenative.models.RiskResult;
import com.securenative.utils.Logger;
import com.securenative.utils.Utils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.asynchttpclient.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EventManager implements IEventManager {
    private final String USER_AGENT_VALUE = "com.securenative.snlogic.SecureNative-java";
    private final String SN_VERSION = "SN-Version";
    private BoundRequestBuilder asyncClient;
    private String apiKey;
    private ExecutorService executor;
    private ConcurrentLinkedQueue<Message> events;
    private ObjectMapper mapper;
    private int HTTP_STATUS_OK = 201;
    private String AUTHORIZATION = "Authorization";
    private SecureNativeOptions options;
    RiskResult defaultRiskResult = new RiskResult(RiskLevel.low.name(), 0.0, ActionType.ALLOW.toString());

    public EventManager(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        this.options = options;
        events = new ConcurrentLinkedQueue<>();
        if (Utils.isNullOrEmpty(apiKey) || options == null) {
            throw new SecureNativeSDKException("You must pass a valid api key");
        }
        this.asyncClient = initializeAsyncHttpClient(options);
        this.apiKey = apiKey;

        if (this.options.getSdkEnabled() != null && !this.options.getSdkEnabled()) {
            executor = Executors.newSingleThreadScheduledExecutor();
            Logger.getLogger().debug("Starting thread listening to messages queue");
            executor.execute(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                    Message msg = events.poll();
                    if (msg != null) {
                        sendSync(msg.getEvent(), msg.getUrl());
                    }
                } catch (InterruptedException e) {
                    Logger.getLogger().debug("Stopping event manager", e);
                }
            });
        }
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Override
    public RiskResult sendSync(Event event, String url) {
        if (this.options.getSdkEnabled() != null && !this.options.getSdkEnabled()) {
            return defaultRiskResult;
        }

        this.asyncClient.setHeader(AUTHORIZATION, this.apiKey).setUrl(url);

        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
            Response response = this.asyncClient.execute().get();
            if (response == null || response.getStatusCode() > HTTP_STATUS_OK) {
                Logger.getLogger().debug(String.format("SecureNative http call failed to end point: %s  with event type %s. adding back to queue.", url, event.getEventType()));
                events.add(new Message(event, response.getUri().toUrl()));
            }
            String responseBody = response.getResponseBody();
            if (Utils.isNullOrEmpty(responseBody)) {
                Logger.getLogger().debug(String.format("SecureNative http call to %s returned with empty response. returning default risk result.", url));
                return defaultRiskResult;
            }
            Logger.getLogger().debug(String.format("SecureNative http call to %s was successful.", url));
            return mapper.readValue(responseBody, RiskResult.class);
        } catch (InterruptedException | ExecutionException | IOException e) {
            Logger.getLogger().debug(String.format("Failed to send event; %s", e));
        }
        return defaultRiskResult;
    }

    @Override
    public void sendAsync(Event event, String url) {
        if (this.options.getSdkEnabled() != null && !this.options.getSdkEnabled()) {
            return;
        }
        this.asyncClient.setUrl(url).setHeader(AUTHORIZATION, this.apiKey);
        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            Logger.getLogger().debug(String.format("SecureNative async http call failed to end point: %s with event type %s. error: %s", url, event.getEventType(), e));
        }

        this.asyncClient.execute(
                new AsyncCompletionHandler<>() {
                    @Override
                    public Object onCompleted(Response response) {
                        if (response.getStatusCode() > HTTP_STATUS_OK) {
                            Logger.getLogger().debug(String.format("SecureNative http call failed to end point: %s with event type %s. adding back to queue.", url, event.getEventType()));
                            events.add(new Message(event, response.getUri().toUrl()));
                        }
                        return response;
                    }
                });
    }

    @Override
    public String sendAgentEvent(Event event, String requestUrl) {
        this.asyncClient.setHeader(AUTHORIZATION, this.apiKey).setUrl(requestUrl);
        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
            Response response = this.asyncClient.execute().get();
            if (response == null || response.getStatusCode() > HTTP_STATUS_OK) {
                Logger.getLogger().debug(String.format("SecureNative http call failed to end point: %s  with event type %s. adding back to queue.", requestUrl, event.getEventType()));
                assert response != null;
                events.add(new Message(event, response.getUri().toUrl()));
            }
            String responseBody = response.getResponseBody();
            if (Utils.isNullOrEmpty(responseBody)) {
                Logger.getLogger().debug(String.format("SecureNative http call to %s returned with empty response. returning default risk result.", requestUrl));
                return "";
            }
            Logger.getLogger().debug(String.format("SecureNative http call to %s was successful.", requestUrl));
            return responseBody;
        } catch (InterruptedException | ExecutionException | IOException e) {
            Logger.getLogger().debug(String.format("Failed to send event; %s", e));
        }
        return "";
    }

    public AgentConfigOptions sendUpdateConfigEvent(Event event, String requestUrl) {
        this.asyncClient.setHeader(AUTHORIZATION, this.apiKey).setUrl(requestUrl);
        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
            Response response = this.asyncClient.execute().get();
            if (response == null || response.getStatusCode() > HTTP_STATUS_OK) {
                Logger.getLogger().debug(String.format("SecureNative http call failed to end point: %s  with event type %s. adding back to queue.", requestUrl, event.getEventType()));
            }
            assert response != null;
            String responseBody = response.getResponseBody();
            if (Utils.isNullOrEmpty(responseBody)) {
                Logger.getLogger().debug(String.format("SecureNative http call to %s returned with empty response. returning default risk result.", requestUrl));
                return null;
            }
            Logger.getLogger().debug(String.format("SecureNative http call to %s was successful.", requestUrl));
            return mapper.readValue(responseBody, AgentConfigOptions.class);
        } catch (InterruptedException | ExecutionException | IOException e) {
            Logger.getLogger().debug(String.format("Failed to send event; %s", e));
        }
        return null;
    }

    private BoundRequestBuilder initializeAsyncHttpClient(SecureNativeOptions options) {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout((int) options.getTimeout())
                .setUserAgent(USER_AGENT_VALUE);
        AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
        Logger.getLogger().debug("Initialized Http client");
        return client.preparePost(options.getApiUrl())
                .addHeader(SN_VERSION, this.getVersion()).addHeader("Accept", "application/json");

    }

    private String getVersion() {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            String pomResource = "/META-INF/maven/com.securenative.java/sdk-base/pom.xml";
            Model read = reader.read(new InputStreamReader(EventManager.class.getResourceAsStream(pomResource)));
            return read.getParent().getVersion();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public void flush() {
        Logger.getLogger().debug("Flushing event queue");
        for (Message message : this.events) {
            this.sendSync(message.getEvent(), message.getUrl());
        }
    }
}