package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.repository.CameraSnapshotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class CameraStreamService {

    private CameraSnapshotRepository snapshotRepository;

    public void pushCameraSnapshot(String cameraId, Consumer<byte[]> consumer) {
        while (true) {
            snapshotRepository.getLastSnapshotByCameraId(cameraId)
                    .ifPresent(consumer);
        }
    }
}
