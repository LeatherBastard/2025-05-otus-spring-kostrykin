package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.MessageMapper;
import ru.otus.hw.dto.message.CreateMessageDto;
import ru.otus.hw.dto.message.MessageDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Message;
import ru.otus.hw.repositories.MessageRepository;
import ru.otus.hw.repositories.TopicRepository;

@RequiredArgsConstructor
@Service
@Log4j2
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    private final TopicRepository topicRepository;

    private final MessageMapper messageMapper;


    @Override
    public Flux<MessageDto> findAllByTopicId(String topicId) {
        return topicRepository.findById(topicId)
                .switchIfEmpty(
                        Mono.error(
                                new EntityNotFoundException(String.format("Topic with id %s was not found",topicId))))
                .thenMany(messageRepository.findAllByTopicId(topicId))
                .map(messageMapper::messageToDto);

    }

    @Override
    public Mono<MessageDto> insertMessage(CreateMessageDto messageDto) {
        return Mono.just(messageDto).flatMap(dto -> {
                    Message message = new Message(messageDto.topicId(), messageDto.role(), messageDto.content());
                    return messageRepository.save(message)
                            .map(messageMapper::messageToDto);
                }
        );
    }

}
