package ru.otus.hw.services.ollama;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.ollama.ModelResponse;
import ru.otus.hw.dto.ollama.UserRequest;


@Service
@Log4j2
public class OllamaServiceImpl implements OllamaService {


    private final WebClient webClient;

    public OllamaServiceImpl(
            @Qualifier("ollamaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<ModelResponse> sendMessage(UserRequest request) {
        return webClient.post().uri("/api/chat")
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ModelResponse.class)
                .doOnSubscribe(subscription -> log.info("Getting stream data"))
                .doOnNext(response -> log.info("Stream data part: {}", response.getMessage().getContent()))
                .doOnComplete(() -> log.info("Stream data finished"))
                .doOnError(error -> log.error("Error getting stream data: {}", error.getMessage()));
    }

}
