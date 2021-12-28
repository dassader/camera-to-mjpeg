package com.home.cameratomjpeg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CameraToMjpegApplication {

    public static void main(String[] args) {
        SpringApplication.run(CameraToMjpegApplication.class, args);
    }

}
