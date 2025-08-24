package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreMapper genreMapper;

    private final GenreRepository genreRepository;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll().map(genreMapper::genreToDto);
    }
}
