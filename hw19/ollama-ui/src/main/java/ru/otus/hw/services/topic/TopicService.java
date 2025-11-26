package ru.otus.hw.services.topic;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.topic.CreateTopicDto;
import ru.otus.hw.dto.topic.TopicDto;

public interface TopicService {
    Mono<TopicDto> insertTopic(String token, CreateTopicDto topicDto);

    Flux<TopicDto> findTopicsByUserId(String token, String userId);
}
