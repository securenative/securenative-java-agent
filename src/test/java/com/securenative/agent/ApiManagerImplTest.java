package com.securenative.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.agent.context.SecureNativeContext;
import com.securenative.agent.enums.EventTypes;
import com.securenative.agent.events.manager.SecureNativeEventManager;
import com.securenative.agent.exceptions.SecureNativeInvalidOptionsException;
import com.securenative.agent.models.EventOptions;
import com.securenative.agent.models.UserTraits;
import com.securenative.agent.models.VerifyResult;
import com.securenative.agent.config.ConfigurationManager;
import com.securenative.agent.enums.RiskLevel;
import com.securenative.agent.events.manager.ApiManager;
import com.securenative.agent.events.manager.ApiManagerImpl;
import com.securenative.agent.exceptions.SecureNativeSDKException;
import com.securenative.agent.http.HTTPServerMock;
import com.securenative.agent.utils.EventOptionsBuilder;
import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApiManagerImplTest extends HTTPServerMock {
    private final EventOptions eventOptions;

    public ApiManagerImplTest() throws SecureNativeInvalidOptionsException {
        // init default event for tests
        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405");
        SecureNativeContext context = SecureNative.contextBuilder()
                .ip("127.0.0.1")
                .clientToken("SECURED_CLIENT_TOKEN")
                .headers(headers)
                .build();

        Map<Object, Object> properties = new HashMap<>();
        properties.put("prop1", "CUSTOM_PARAM_VALUE");
        properties.put("prop2", true);
        properties.put("prop3", 3);
         eventOptions = EventOptionsBuilder.builder(EventTypes.LOG_IN)
                .userId("USER_ID")
                .userTraits(new UserTraits("'USER_NAME'", "'USER_EMAIL'"))
                .context(context)
                .properties(properties)
                .timestamp(new Date())
                .build();
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should call track event")
    public void ShouldCallTrackEventTest() throws SecureNativeSDKException, InterruptedException, JSONException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);

        client = sandbox().mock(200);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();
        ApiManager apiManager = new ApiManagerImpl(eventManager, options);

        try {
            // track async event
            apiManager.track(eventOptions);

            // ensure event to be sent
            RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
            String body = lastRequest != null ? lastRequest.getBody().readUtf8() : null;
            String expected = "{\"eventType\":\"sn.user.login\",\"userId\":\"USER_ID\",\"userTraits\":{\"name\":\"'USER_NAME'\",\"email\":\"'USER_EMAIL'\",\"createdAt\":null},\"request\":{\"cid\":null,\"vid\":null,\"fp\":null,\"ip\":\"127.0.0.1\",\"remoteIp\":null,\"headers\":{\"user-agent\":\"Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405\"},\"url\":null,\"method\":null},\"properties\":{\"prop2\":true,\"prop1\":\"CUSTOM_PARAM_VALUE\",\"prop3\":3}}";
            assertThat(body).isNotNull();
            JSONAssert.assertEquals(expected, body, false);
            assertThat(new JSONObject(body).has("rid")).isTrue();
            assertThat(new JSONObject(body).has("timestamp")).isTrue();
        } finally {
            eventManager.stopEventsPersist();
        }
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should throw when sending more than 10 custom properties to track event")
    public void ShouldThrowWhenSendingMoreThan10CustomPropertiesToTrackEventTest() throws SecureNativeSDKException, SecureNativeInvalidOptionsException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);

        client = sandbox().mock(200);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();
        ApiManager apiManager = new ApiManagerImpl(eventManager, options);

        Map<Object,Object> props = IntStream.range(0,11).boxed().collect(Collectors.toMap(i-> String.format("prop%d",i) , i-> String.format("val%d",i)));

        try {
            assertThrows(SecureNativeInvalidOptionsException.class, ()->{
                // track async event
                apiManager.track(EventOptionsBuilder.builder(EventTypes.LOG_IN)
                        .properties(props)
                        .build());
            });
        }finally {
             eventManager.stopEventsPersist();
        }
    };

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should not call track event when automatic persistence disabled")
    public void ShouldNotCallTrackEventWhenAutomaticPersistenceDisabledTest() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);

        client = sandbox().mock(500);
        eventManager = new SecureNativeEventManager(client, options);
        ApiManager apiManager = new ApiManagerImpl(eventManager, options);

        // track async event
        apiManager.track(eventOptions);

        // ensure event to be sent
        RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);

        assertThat(lastRequest).isNull();
        assertThat(server.getRequestCount()).isEqualTo(0);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should not retry unauthorized track event call")
    public void ShouldNotRetryUnauthorizedTrackEventCallTest() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);

        client = sandbox().mock(401);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();
        ApiManager apiManager = new ApiManagerImpl(eventManager, options);

        // track async event
        apiManager.track(eventOptions);

        try {
            // ensure event to be sent
            RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);

            assertThat(lastRequest).isNotNull();

            // ensure event to be again after backoff
            Thread.sleep(10 * options.getInterval());

            assertThat(server.getRequestCount()).isEqualTo(1);
        } finally {
             eventManager.stopEventsPersist();
        }
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should call verify event")
    public void ShouldCallVerifyEventTest() throws SecureNativeSDKException, JsonProcessingException, InterruptedException, JSONException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY");

        VerifyResult verifyResult = new VerifyResult(RiskLevel.LOW, 0, new String[0]);
        String body = new ObjectMapper().writeValueAsString(verifyResult);

        client = sandbox().mock(200, body);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();
        ApiManager apiManager = new ApiManagerImpl(eventManager, options);


        // call verify event
        VerifyResult result =  apiManager.verify(eventOptions);

        assertThat(result.getRiskLevel()).isEqualTo(verifyResult.getRiskLevel());
        assertThat(result.getScore()).isEqualTo(verifyResult.getScore());
        assertThat(result.getTriggers().length).isEqualTo(verifyResult.getTriggers().length);

        RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
        String lastRequestBody = lastRequest != null ? lastRequest.getBody().readUtf8() : null;
        String expected = "{\"eventType\":\"sn.user.login\",\"userId\":\"USER_ID\",\"userTraits\":{\"name\":\"'USER_NAME'\",\"email\":\"'USER_EMAIL'\",\"createdAt\":null},\"request\":{\"cid\":null,\"vid\":null,\"fp\":null,\"ip\":\"127.0.0.1\",\"remoteIp\":null,\"headers\":{\"user-agent\":\"Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405\"},\"url\":null,\"method\":null},\"properties\":{\"prop2\":true,\"prop1\":\"CUSTOM_PARAM_VALUE\",\"prop3\":3}}";
        assertThat(lastRequestBody).isNotNull();
        JSONAssert.assertEquals(expected, lastRequestBody, false);
        assertThat(new JSONObject(lastRequestBody).has("rid")).isTrue();
        assertThat(new JSONObject(lastRequestBody).has("timestamp")).isTrue();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should fail verify event call when unauthorized")
    public void ShouldFailVerifyEventCallWhenUnauthorizedTest() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY");

        client = sandbox().mock(401);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();
        ApiManager apiManager = new ApiManagerImpl(eventManager, options);

        // call verify event
        VerifyResult verifyResult =  apiManager.verify(eventOptions);
        assertThat(verifyResult.getRiskLevel()).isEqualTo(RiskLevel.LOW);
        assertThat(verifyResult.getScore()).isEqualTo(0);
        assertThat(verifyResult.getTriggers().length).isEqualTo(0);

        assertThat(verifyResult).isNotNull();

        RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
        String lastRequestBody = lastRequest != null ? lastRequest.getBody().readUtf8() : null;

        String expected = "{\"eventType\":\"sn.user.login\",\"userId\":\"USER_ID\",\"userTraits\":{\"name\":\"'USER_NAME'\",\"email\":\"'USER_EMAIL'\",\"createdAt\":null},\"request\":{\"cid\":null,\"vid\":null,\"fp\":null,\"ip\":\"127.0.0.1\",\"remoteIp\":null,\"headers\":{\"user-agent\":\"Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405\"},\"url\":null,\"method\":null},\"properties\":{\"prop2\":true,\"prop1\":\"CUSTOM_PARAM_VALUE\",\"prop3\":3}}";
        assertThat(lastRequestBody).isNotNull();
    }
}
