package ru.otus.hw.resositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Message;
import ru.otus.hw.models.Topic;


public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
    Flux<Message> findAllByTopicId(String topicId);
}
