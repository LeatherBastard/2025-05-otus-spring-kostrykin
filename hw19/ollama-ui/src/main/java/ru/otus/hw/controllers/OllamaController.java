package ru.otus.hw.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.ollama.ModelResponse;
import ru.otus.hw.dto.ollama.UserRequest;
import ru.otus.hw.services.ollama.OllamaService;

@RestController
@RequestMapping("api/chats")
@RequiredArgsConstructor
@Log4j2
public class OllamaController {

    private final OllamaService ollamaService;

    @PostMapping
    public Flux<ModelResponse> sendMessage(@RequestBody UserRequest request) {
        return ollamaService.sendMessage(request);
    }
}
