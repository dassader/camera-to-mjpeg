package com.home.cameratomjpeg.client;

import com.home.cameratomjpeg.config.ApplicationConfigEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
@AllArgsConstructor
public class HassClient {
    private CloseableHttpClient httpClient;
    private ApplicationConfigEntity applicationConfig;

    public CloseableHttpResponse getCameraSnapshot(String cameraId) {
        URI uri;
        try {
            uri = new URIBuilder(applicationConfig.getBaseUrl())
                    .setPathSegments("api", "camera_proxy", cameraId)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Error while build url", e);
        }

        HttpGet request = new HttpGet(uri);
        request.setHeader("Authorization", "Bearer " + applicationConfig.getToken());

        try {
            return httpClient.execute(request);
        } catch (IOException e) {
            throw new IllegalStateException("Error while read camera snapshot", e);
        }
    }

    public void triggerWebhook(String webhookId, NameValuePair... nvps) {
        log.info("Trigger webhook: {}", webhookId);

        URI uri;
        try {
            uri = new URIBuilder(applicationConfig.getBaseUrl())
                    .setPathSegments("api", "webhook", webhookId)
                    .setParameters(nvps)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Error while build url", e);
        }

        HttpPost request = new HttpPost(uri);
        request.setHeader("Authorization", "Bearer " + applicationConfig.getToken());


        try (CloseableHttpResponse response = httpClient.execute(request)) {

        } catch (IOException e) {
            throw new IllegalStateException("Error trigger webhook", e);
        }
    }
}
