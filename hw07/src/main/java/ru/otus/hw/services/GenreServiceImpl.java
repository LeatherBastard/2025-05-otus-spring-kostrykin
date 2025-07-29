package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreConverter genreConverter;

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        List<GenreDto> genres = new ArrayList<>();
        genreRepository.findAll().forEach(
                genre -> {
                    genres.add(genreConverter.genreToDto(genre));
                }
        );
        return genres;
    }
}
