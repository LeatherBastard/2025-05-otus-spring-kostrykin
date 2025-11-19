package ru.otus.hw.services.topic;

import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.topic.CreateTopicDto;
import ru.otus.hw.dto.topic.TopicDto;

public interface TopicService {
    Mono<TopicDto> insertTopic(CreateTopicDto topicDto);
    Flux<TopicDto> findTopicsByUserId(String userId);
}
