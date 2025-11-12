package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.models.ModelResponse;
import ru.otus.hw.models.UserRequest;


public interface OllamaService {
    Flux<ModelResponse> sendMessage(UserRequest request);
}
