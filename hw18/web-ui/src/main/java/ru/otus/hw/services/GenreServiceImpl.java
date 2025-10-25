package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.hw.clients.GenreClient;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.genre.GenreDto;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
@Retry(name = "uiRetryService")
@CircuitBreaker(name = "uiCircuitBreakerService",fallbackMethod = "fallbackResponse")
public class GenreServiceImpl implements GenreService {
    private final GenreClient genreClient;

    @Override
    public List<GenreDto> findAll() {
        return genreClient.getGenres();
    }

    public List<GenreDto> fallbackResponse(Throwable ex) {
        log.error(ex.getMessage());
        return List.of();
    }
}
