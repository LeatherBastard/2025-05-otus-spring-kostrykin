package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CreateMessageDto;
import ru.otus.hw.dto.MessageDto;
import ru.otus.hw.services.MessageService;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;


    @PostMapping
    public Mono<MessageDto> addMessage(@RequestBody CreateMessageDto messageDto) {
        return messageService.insertMessage(messageDto);
    }

    @GetMapping
    public Flux<MessageDto> getMessagesByTopic(@RequestParam String topic) {
        return messageService.findAllByTopic(topic);
    }
}
