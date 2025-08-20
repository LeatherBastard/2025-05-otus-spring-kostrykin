package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.dto.genre.GenreDto;

import java.util.List;

public interface GenreService {
    Flux<GenreDto> findAll();
}
