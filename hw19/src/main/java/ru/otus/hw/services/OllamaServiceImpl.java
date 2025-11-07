package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.ModelResponse;
import ru.otus.hw.models.UserRequest;

@RequiredArgsConstructor
@Service
@Log4j2
public class OllamaServiceImpl implements OllamaService {
    private final WebClient webClient;

    @Override
    public Flux<ModelResponse> sendMessage(UserRequest request) {

            return webClient.post().uri("/api/chat").
                    bodyValue(request)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(ModelResponse.class)
                    .doOnSubscribe(subscription -> log.info("Начало получения потоковых данных"))
                    .doOnNext(response -> log.info("Получена часть сообщения: {}", response.getMessage().getContent()))
                    .doOnComplete(() -> log.info("Поток данных завершен"))
                    .doOnError(error -> log.error("Ошибка при получении данных: {}", error.getMessage()));
    }

}
