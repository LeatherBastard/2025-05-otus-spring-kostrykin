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
import ru.otus.hw.services.TopicService;

@RestController
@RequestMapping("api/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public Mono<TopicDto> addTopic(Authentication authentication, @RequestParam  String title) {
        String userId = (String) authentication.getDetails();
        return topicService.insertTopic(new CreateTopicDto(userId, title));
    }

    @GetMapping
    public Flux<TopicDto> getTopicsByUserId(Authentication authentication) {
        String userId = (String) authentication.getDetails();
        return topicService.findAllByUserId(userId);
    }

}
