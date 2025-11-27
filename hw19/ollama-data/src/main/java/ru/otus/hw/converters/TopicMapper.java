package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.topic.TopicDto;
import ru.otus.hw.models.Topic;

@Component
public class TopicMapper {
    public TopicDto topicToDto(Topic topic) {
        return new TopicDto(topic.getId(), topic.getTitle());
    }
}
