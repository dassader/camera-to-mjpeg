package com.home.cameratomjpeg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;

@Configuration
public class ApplicationConfig {
    @Bean
    @Profile("!local")
    public ApplicationConfigEntity applicationConfigEntity(ObjectMapper objectMapper, ResourceLoader resourceLoader) throws IOException {
        File file = resourceLoader.getResource("file:./data/options.json").getFile();
        return objectMapper.readValue(file, ApplicationConfigEntity.class);
    }
}
