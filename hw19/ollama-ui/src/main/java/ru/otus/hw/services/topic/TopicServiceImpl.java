package ru.otus.hw.services.topic;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.topic.CreateTopicDto;
import ru.otus.hw.dto.topic.TopicDto;

@Service
public class TopicServiceImpl implements TopicService {
    private final WebClient webClient;

    public TopicServiceImpl(
            @Qualifier("ollamaDataWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<TopicDto> insertTopic(String token, CreateTopicDto topicDto) {
        return webClient.post().uri(
                        uriBuilder -> uriBuilder
                                .path("api/topics")
                                .queryParam("userId", topicDto.userId())
                                .queryParam("title", topicDto.title())
                                .build())
                .cookie("AUTH_TOKEN", token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TopicDto.class);
    }

    @Override
    public Flux<TopicDto> findTopicsByUserId(String token, String userId) {
        return webClient.get().uri(
                        uriBuilder -> uriBuilder
                                .path("api/topics")
                                .queryParam("userId", userId)
                                .build()
                )
                .cookie("AUTH_TOKEN", token)
                .retrieve()
                .bodyToFlux(TopicDto.class);
    }
}
