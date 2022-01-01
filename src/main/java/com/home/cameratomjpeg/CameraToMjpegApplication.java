package com.home.cameratomjpeg;

import com.home.cameratomjpeg.config.AddonContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CameraToMjpegApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CameraToMjpegApplication.class)
                .initializers(new AddonContextInitializer())
                .run();
    }

}
