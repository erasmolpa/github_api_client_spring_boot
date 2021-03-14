package com.githubclient.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestApiConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiConfig.class);

    @Value("${GITHUB_API.CONNECT_TIMEOUT}")
    private int CONNECT_TIMEOUT=0;

    @Value("${GITHUB_API.READ_TIMEOUT}")
    private int READ_TIMEOUT=0;

    @Value("${GITHUB_API.URL}")
    private String API_URL;
    private final String SEARCH_URI_PATH = "search/users?";

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder.setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
                .setReadTimeout(Duration.ofMillis(READ_TIMEOUT))
                .rootUri(API_URL)
                .build();
    }
}

