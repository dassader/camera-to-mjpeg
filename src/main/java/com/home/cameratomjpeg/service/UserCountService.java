package com.home.cameratomjpeg.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserCountService {
    private AtomicInteger atomicInteger;

    public UserCountService() {
        this.atomicInteger = new AtomicInteger();
    }

    public int userConnect() {
        return atomicInteger.incrementAndGet();
    }

    public int userDisconnect() {
        return atomicInteger.decrementAndGet();
    }
}
