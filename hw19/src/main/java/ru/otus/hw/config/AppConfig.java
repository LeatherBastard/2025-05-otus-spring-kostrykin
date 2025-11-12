package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${ollama.url}")
    private String ollamaUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(ollamaUrl)
                .build();
    }
}
