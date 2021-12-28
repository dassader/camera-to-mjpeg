package com.home.cameratomjpeg.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationConfigEntity {
    private String baseUrl;
    private String token;
    private String cameraHasConnectionWebhook;
    private String cameraHasNoConnectionWebhook;
    private List<String> cameraIdList = new ArrayList<>();
    private Integer socketTimeout;
    private String telegramBotToken;
}
