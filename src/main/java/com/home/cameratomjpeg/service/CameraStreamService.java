package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.config.ApplicationConfigEntity;
import lombok.AllArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;

@Service
@AllArgsConstructor
public class CameraStreamService {

    private HttpClient httpClient;
    private ApplicationConfigEntity applicationConfigEntity;

    public void writeCameraSnapshot(String cameraId, BiConsumer<InputStream, Long> partsConsumer) {
        while (true) {
            HttpEntity response = readCameraSnapshot(cameraId);
            long contentLength = response.getContentLength();
            try {
                InputStream content = response.getContent();
                partsConsumer.accept(content, contentLength);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private HttpEntity readCameraSnapshot(String cameraId) {
        String url = applicationConfigEntity.getBaseUrl() + "/api/camera_proxy/" + cameraId;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer "+ applicationConfigEntity.getToken());

        try {
            HttpResponse response = httpClient.execute(httpGet);
            return response.getEntity();
        } catch (IOException e) {
            throw new IllegalStateException("Error while read");
        }
    }
}
