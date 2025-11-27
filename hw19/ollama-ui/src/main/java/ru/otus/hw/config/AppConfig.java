package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${ollama.url}")
    private String ollamaUrl;

    @Value("${ollama-data.url}")
    private String ollamaDataUrl;

    @Bean
    public WebClient ollamaWebClient() {
        return WebClient.builder()
                .baseUrl(ollamaUrl)
                .build();
    }

    @Bean
    public WebClient ollamaDataWebClient() {
        return WebClient.builder()
                .baseUrl(ollamaDataUrl)
                .build();
    }

}
