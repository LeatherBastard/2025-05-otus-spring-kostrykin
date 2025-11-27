package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.message.CreateMessageDto;
import ru.otus.hw.dto.message.MessageDto;

public interface MessageService {

    Flux<MessageDto> findAllByTopicId(String topicId);

    Mono<MessageDto> insertMessage(CreateMessageDto messageDto);

}
