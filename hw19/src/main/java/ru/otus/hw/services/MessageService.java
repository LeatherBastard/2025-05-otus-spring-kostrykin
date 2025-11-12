package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CreateMessageDto;
import ru.otus.hw.dto.MessageDto;

public interface MessageService {

    Flux<MessageDto> findAllByTopic(String topic);

    Mono<MessageDto> insertMessage(CreateMessageDto messageDto);

}
