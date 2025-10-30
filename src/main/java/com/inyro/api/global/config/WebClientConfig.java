package com.inyro.api.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String BASE_URL = "https://smul.smu.ac.kr";

    @Bean(name = "clubWebClient")
    public WebClient clubWebClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }
}

