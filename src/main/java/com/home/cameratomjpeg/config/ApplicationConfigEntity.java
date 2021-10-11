package com.home.cameratomjpeg.config;

import lombok.Data;

@Data
public class ApplicationConfigEntity {
    private String baseUrl;
    private String token;
    private String cameraHasConnectionWebhook;
    private String cameraHasNoConnectionWebhook;
    private Integer socketTimeout;
}
