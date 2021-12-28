package com.home.cameratomjpeg.service;

import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.ContentTypes;

public class SendPhotosTelegramAction extends AbstractSendRequest<SendPhotosTelegramAction> {
    private int counter = 0;

    public SendPhotosTelegramAction(String chatId) {
        super(chatId);
    }

    public void addPhoto(byte[] content) {
        counter++;
        add("photo-" + counter, content);
    }

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public String getFileName() {
        counter++;
        return "photo-" + counter + ".jpg";
    }

    @Override
    public String getContentType() {
        return ContentTypes.PHOTO_MIME_TYPE;
    }
}
