package com.home.cameratomjpeg.repository;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CameraSnapshotRepository {
    private Map<String, byte[]> lastSnapshotStorage = new HashMap<>();
    private Map<String, CircularFifoQueue<byte[]>> snapshotHistoryStorage = new HashMap<>();

    public void saveLastSnapshotByCameraId(String cameraId, byte[] content) {
        lastSnapshotStorage.put(cameraId, content);
    }

    public Optional<byte[]> getLastSnapshotByCameraId(String cameraId) {
        return Optional.ofNullable(lastSnapshotStorage.get(cameraId));
    }

    public Collection<byte[]> getHistoryByCameraId(String cameraId) {
        return snapshotHistoryStorage.get(cameraId);
    }

    public void saveLastSnapshotByCameraId(String cameraId) {
        byte[] bytes = lastSnapshotStorage.get(cameraId);

        if (bytes == null) {
            return;
        }

        CircularFifoQueue<byte[]> queue = snapshotHistoryStorage
                .computeIfAbsent(cameraId, target -> new CircularFifoQueue<>(5));

        queue.add(bytes);
    }
}
