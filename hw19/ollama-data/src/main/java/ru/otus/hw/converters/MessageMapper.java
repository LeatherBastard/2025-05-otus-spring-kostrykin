package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.message.MessageDto;
import ru.otus.hw.models.Message;


@Component
public class MessageMapper {
    public MessageDto messageToDto(Message message) {
        return new MessageDto(message.getRole(), message.getContent());
    }
}

