package ru.otus.hw.resositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Message;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
    Flux<Message> findAllByTopic(String topic);
}
