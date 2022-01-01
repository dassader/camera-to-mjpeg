package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.repository.CameraSnapshotRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CameraSnapshotHistoryLoader implements Runnable {

    private List<String> cameraIdList;
    private CameraSnapshotRepository snapshotRepository;

    public CameraSnapshotHistoryLoader(@Value("${cameraIdList}") List<String> cameraIdList,
                                       CameraSnapshotRepository snapshotRepository) {
        this.cameraIdList = cameraIdList;
        this.snapshotRepository = snapshotRepository;
    }

    @Scheduled(fixedDelay = 1_500, initialDelay = 1_000)
    public void run() {
        for (String cameraId : cameraIdList) {
            snapshotRepository.saveLastSnapshotByCameraId(cameraId);
        }
    }
}
