package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.topic.CreateTopicDto;
import ru.otus.hw.dto.topic.TopicDto;

public interface TopicService {
    Flux<TopicDto> findAllByUserId(String userId);

    Mono<TopicDto> insertTopic(CreateTopicDto createTopicDto);
}
