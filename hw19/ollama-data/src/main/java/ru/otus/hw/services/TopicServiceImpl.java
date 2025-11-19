package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.TopicMapper;
import ru.otus.hw.dto.topic.CreateTopicDto;
import ru.otus.hw.dto.topic.TopicDto;
import ru.otus.hw.models.Topic;
import ru.otus.hw.resositories.TopicRepository;

@RequiredArgsConstructor
@Service
@Log4j2
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Override
    public Flux<TopicDto> findAllByUserId(String userId) {
        return topicRepository.findAllByUserId(userId).map(topicMapper::topicToDto);
    }

    @Override
    public Mono<TopicDto> insertTopic(CreateTopicDto createTopicDto) {
        return Mono.just(createTopicDto).flatMap(dto ->
                {
                    Topic topic = new Topic(createTopicDto.userId(), createTopicDto.name());
                    return topicRepository.save(topic).map(topicMapper::topicToDto);
                }
        );
    }
}
