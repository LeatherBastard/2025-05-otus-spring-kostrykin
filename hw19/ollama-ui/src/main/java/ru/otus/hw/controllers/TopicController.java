package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.topic.CreateTopicDto;
import ru.otus.hw.dto.topic.TopicDto;
import ru.otus.hw.services.topic.TopicService;

@RestController
@RequestMapping("api/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public Mono<TopicDto> addTopic(Authentication authentication, @RequestParam String title) {
        String userId = (String) authentication.getDetails();
        CreateTopicDto topicDto = new CreateTopicDto(userId, title);
        String token = (String) authentication.getCredentials();
        return topicService.insertTopic(token, topicDto);
    }

    @GetMapping
    public Flux<TopicDto> getTopicsByUser(Authentication authentication) {
        String userId = (String) authentication.getDetails();
        String token = (String) authentication.getCredentials();
        return topicService.findTopicsByUserId(token, userId);
    }

}
