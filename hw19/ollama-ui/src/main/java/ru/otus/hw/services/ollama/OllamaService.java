package ru.otus.hw.services.ollama;

import reactor.core.publisher.Flux;
import ru.otus.hw.dto.ollama.ModelResponse;
import ru.otus.hw.dto.ollama.UserRequest;


public interface OllamaService {
    Flux<ModelResponse> sendMessage(UserRequest request);
}
