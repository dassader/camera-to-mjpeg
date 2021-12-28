package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.config.ApplicationConfigEntity;
import com.home.cameratomjpeg.repository.CameraSnapshotRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CameraSnapshotHistoryLoader implements Runnable {

    private ApplicationConfigEntity applicationConfig;
    private CameraSnapshotRepository snapshotRepository;

    @Scheduled(fixedDelay = 1_500, initialDelay = 1_000)
    public void run() {
        for (String cameraId : applicationConfig.getCameraIdList()) {
            snapshotRepository.saveLastSnapshotByCameraId(cameraId);
        }
    }
}
