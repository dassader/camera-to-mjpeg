package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.client.HassClient;
import com.home.cameratomjpeg.repository.CameraSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class CameraSnapshotLoader implements Runnable {

    private HassClient hassClient;
    private CameraSnapshotRepository snapshotRepository;
    private List<String> cameraIdList;

    public CameraSnapshotLoader(HassClient hassClient,
                                CameraSnapshotRepository snapshotRepository,
                                @Value("${cameraIdList}") List<String> cameraIdList) {
        this.hassClient = hassClient;
        this.snapshotRepository = snapshotRepository;
        this.cameraIdList = cameraIdList;
    }

    @Override
    @Scheduled(fixedDelay = 1_000)
    public void run() {
        log.info("Init snapshot loader");

        try {
            while (true) {
                cameraIdList.forEach(this::loadSnapshotByCameraId);
            }
        } catch (Exception e) {
            log.error("Error while load snapshot with message: {}", e.getMessage());
        }
    }

    private void loadSnapshotByCameraId(String cameraId) {
        try (CloseableHttpResponse response = hassClient.getCameraSnapshot(cameraId)) {
            HttpEntity entity = response.getEntity();
            long contentLength = entity.getContentLength();
            byte[] content = new byte[(int) contentLength];
            IOUtils.readFully(entity.getContent(), content);

            snapshotRepository.saveLastSnapshotByCameraId(cameraId, content);
        } catch (IOException e) {
            log.error("Error while load snapshot with message: {}", e.getMessage());
            throw new IllegalStateException(e);
        }
    }
}
