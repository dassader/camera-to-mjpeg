package com.home.cameratomjpeg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.Map;

@Slf4j
public class AddonContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private ObjectMapper objectMapper;

    public AddonContextInitializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();

        String configResourcePath = getConfigResourcePath(environment);
        Resource configResource = context.getResource(configResourcePath);

        if(!configResource.exists()) {
            throw new IllegalStateException("Config file not found: "+configResourcePath);
        }

        try {
            Map map = objectMapper.readValue(configResource.getFile(), Map.class);
            context.getEnvironment()
                    .getPropertySources()
                    .addFirst(new MapPropertySource("addon", map));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String getConfigResourcePath(Environment environment) {
        boolean localProfile = Arrays.asList(environment.getActiveProfiles()).contains("local");

        if(localProfile) {
            return "classpath:local-options.json";
        } else {
            return "file:./data/options.json";
        }
    }
}
