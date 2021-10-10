package com.home.cameratomjpeg.service;

import com.home.cameratomjpeg.client.HassClient;
import com.home.cameratomjpeg.config.ApplicationConfigEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserCountService {
    private AtomicInteger atomicInteger;
    private ApplicationConfigEntity applicationConfig;
    private HassClient hassClient;

    public UserCountService(ApplicationConfigEntity applicationConfig, HassClient hassClient) {
        this.applicationConfig = applicationConfig;
        this.hassClient = hassClient;
        this.atomicInteger = new AtomicInteger();
    }

    public int fireUserConnect() {
        int userCount = atomicInteger.incrementAndGet();

        triggerWebhookIfExist(applicationConfig.getCameraHasConnectionWebhook(), userCount);

        return userCount;
    }

    public int fireUserDisconnect() {
        int userCount = atomicInteger.decrementAndGet();

        if(userCount <= 0) {
            triggerWebhookIfExist(applicationConfig.getCameraHasNoConnectionWebhook(), userCount);
        }

        return userCount;
    }

    private void triggerWebhookIfExist(String webhookId, int userCount) {
        if(webhookId == null || webhookId.isEmpty()) {
            return;
        }

        BasicNameValuePair webhookTriggerParameter = new BasicNameValuePair("userCount", String.valueOf(userCount));
        hassClient.triggerWebhook(webhookId, webhookTriggerParameter);
    }
}
