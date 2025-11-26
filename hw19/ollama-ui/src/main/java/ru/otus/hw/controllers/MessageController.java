package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.message.CreateMessageDto;
import ru.otus.hw.dto.message.MessageDto;
import ru.otus.hw.services.message.MessageService;

@RestController
@RequestMapping("api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public Mono<MessageDto> addMessage(Authentication authentication, @RequestBody @Valid CreateMessageDto messageDto) {
        String token = (String) authentication.getCredentials();
        return messageService.insertMessage(token, messageDto);
    }

    @GetMapping
    public Flux<MessageDto> getMessagesByTopicId(Authentication authentication, @RequestParam String topicId) {
        String token = (String) authentication.getCredentials();
        return messageService.findMessagesByTopicId(token, topicId);
    }


}
