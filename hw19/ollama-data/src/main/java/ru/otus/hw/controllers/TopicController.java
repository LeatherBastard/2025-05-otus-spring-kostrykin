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
import ru.otus.hw.dto.topic.CreateTopicDto;
import ru.otus.hw.dto.topic.TopicDto;
import ru.otus.hw.services.TopicService;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public Mono<TopicDto> addMessage(@RequestBody CreateTopicDto topicDto) {
        return topicService.insertTopic(topicDto);
    }

    @GetMapping
    public Flux<TopicDto> getTopicsByUserId(@RequestParam String userId) {
        return topicService.findAllByUserId(userId);
    }
}
