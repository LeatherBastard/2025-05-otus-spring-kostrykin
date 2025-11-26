package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Topic;


public interface TopicRepository extends ReactiveMongoRepository<Topic, String> {
    Flux<Topic> findAllByUserId(String userId);
}
