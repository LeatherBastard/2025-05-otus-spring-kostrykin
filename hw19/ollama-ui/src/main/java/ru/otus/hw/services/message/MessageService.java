package ru.otus.hw.services.message;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.message.CreateMessageDto;
import ru.otus.hw.dto.message.MessageDto;

public interface MessageService {
    Mono<MessageDto> insertMessage(String token, CreateMessageDto messageDto);

    Flux<MessageDto> findMessagesByTopicId(String token, String topicId);

}
