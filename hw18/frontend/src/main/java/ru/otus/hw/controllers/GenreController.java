package ru.otus.hw.controllers;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    @RateLimiter(name = "frontendRateLimitedService", fallbackMethod = "rateLimitFallBack")
    public List<GenreDto> getGenres() {
        return genreService.findAll();
    }

    private List<GenreDto> rateLimitFallBack(RequestNotPermitted e) {
        throw e;
    }
}
