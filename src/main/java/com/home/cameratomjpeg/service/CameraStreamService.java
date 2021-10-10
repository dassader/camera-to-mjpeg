package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.client.HassClient;
import lombok.AllArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;

@Service
@AllArgsConstructor
public class CameraStreamService {

    private HassClient hassClient;

    public void writeCameraSnapshot(String cameraId, BiConsumer<InputStream, Long> partsConsumer) {
        while (true) {
            try (CloseableHttpResponse response = hassClient.getCameraSnapshot(cameraId)) {
                HttpEntity entity = response.getEntity();
                long contentLength = entity.getContentLength();
                InputStream content = entity.getContent();

                partsConsumer.accept(content, contentLength);
            } catch (IOException e) {
                throw new IllegalStateException("Error while write camera snapshot", e);
            }
        }
    }
}
