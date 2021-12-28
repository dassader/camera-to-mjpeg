package com.home.cameratomjpeg.controller;

import com.home.cameratomjpeg.controller.request.SnapshotHistoryRequest;
import com.home.cameratomjpeg.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @PostMapping("/messages/snapshotHistory")
    public void sendSnapshotHistory(@RequestBody SnapshotHistoryRequest request) {
        messageService.sendCameraSnapshotToTheChat(request.getCameraId(), request.getChatId());
    }
}
