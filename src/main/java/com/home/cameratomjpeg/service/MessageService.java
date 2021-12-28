package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.repository.CameraSnapshotRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InputMediaPhoto;
import com.pengrad.telegrambot.request.SendMediaGroup;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MessageService {

    private TelegramBot telegramBot;
    private CameraSnapshotRepository snapshotRepository;

    public MessageService(@Autowired(required = false) TelegramBot telegramBot,
                          CameraSnapshotRepository snapshotRepository) {
        this.telegramBot = telegramBot;
        this.snapshotRepository = snapshotRepository;
    }

    public void sendCameraSnapshotToTheChat(String cameraId, String chatId) {
        if (telegramBot == null) {
            log.error("Telegram bot not initialized");
            return;
        }

        Collection<byte[]> snapshotHistory = snapshotRepository.getHistoryByCameraId(cameraId);

        if (snapshotHistory == null) {
            log.error("Snapshot history not found for: {}", cameraId);
            return;
        }

        List<InputMediaPhoto> collect = snapshotHistory.stream()
                .map(InputMediaPhoto::new)
                .collect(Collectors.toList());

        InputMediaPhoto[] snapshots = new InputMediaPhoto[collect.size()];
        snapshots = collect.toArray(snapshots);

        telegramBot.execute(new SendMediaGroup(chatId, snapshots));
    }
}
