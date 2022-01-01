package com.home.cameratomjpeg.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
public class HassClient {
    private CloseableHttpClient httpClient;
    private String baseUrl;
    private String token;

    public HassClient(CloseableHttpClient httpClient,
                      @Value("${baseUrl}") String baseUrl,
                      @Value("${token}") String token) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public CloseableHttpResponse getCameraSnapshot(String cameraId) {
        URI uri;
        try {
            uri = new URIBuilder(baseUrl)
                    .setPathSegments("api", "camera_proxy", cameraId)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Error while build url", e);
        }

        HttpGet request = new HttpGet(uri);
        request.setHeader("Authorization", "Bearer " + token);

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
            uri = new URIBuilder(baseUrl)
                    .setPathSegments("api", "webhook", webhookId)
                    .setParameters(nvps)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Error while build url", e);
        }

        HttpPost request = new HttpPost(uri);
        request.setHeader("Authorization", "Bearer " + token);


        try (CloseableHttpResponse response = httpClient.execute(request)) {

        } catch (IOException e) {
            throw new IllegalStateException("Error trigger webhook", e);
        }
    }
}
