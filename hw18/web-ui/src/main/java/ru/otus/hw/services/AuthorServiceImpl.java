package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.hw.clients.AuthorClient;
import ru.otus.hw.dto.author.AuthorDto;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
@Retry(name = "uiRetryService")
@CircuitBreaker(name = "uiCircuitBreakerService", fallbackMethod = "fallbackResponse")
public class AuthorServiceImpl implements AuthorService {
    private final AuthorClient authorClient;


    @Override
    public List<AuthorDto> findAll() {
        return authorClient.getAuthors();
    }

    public List<AuthorDto> fallbackResponse(Throwable ex) {
        log.error(ex.getMessage());
        return List.of();
    }
}
