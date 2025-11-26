package ru.otus.hw.services.message;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.message.CreateMessageDto;
import ru.otus.hw.dto.message.MessageDto;

@Service
public class MessageServiceImpl implements MessageService {
    private final WebClient webClient;

    public MessageServiceImpl(@Qualifier("ollamaDataWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<MessageDto> insertMessage(String token, CreateMessageDto messageDto) {
        return webClient.post().uri("api/messages")
                .cookie("AUTH_TOKEN", token)
                .bodyValue(messageDto)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MessageDto.class);
    }

    @Override
    public Flux<MessageDto> findMessagesByTopicId(String token, String topicId) {
        return webClient.get().uri(
                        uriBuilder -> uriBuilder
                                .path("api/messages")
                                .queryParam("topicId", topicId)
                                .build()
                )
                .cookie("AUTH_TOKEN", token)
                .retrieve()
                .bodyToFlux(MessageDto.class);
    }
}
