package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.MessageMapper;
import ru.otus.hw.dto.CreateMessageDto;
import ru.otus.hw.dto.MessageDto;
import ru.otus.hw.models.Message;
import ru.otus.hw.resositories.MessageRepository;

@RequiredArgsConstructor
@Service
@Log4j2
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public Flux<MessageDto> findAllByTopic(String topic) {
        return messageRepository.findAllByTopic(topic).map(messageMapper::messageToDto);
    }

    @Override
    public Mono<MessageDto> insertMessage(CreateMessageDto messageDto) {
        return Mono.just(messageDto).flatMap(dto ->
                {
                    Message message = new Message(messageDto.topic(), messageDto.role(), messageDto.content());
                    return messageRepository.save(message)
                            .map(messageMapper::messageToDto);
                }
        );
    }


}
