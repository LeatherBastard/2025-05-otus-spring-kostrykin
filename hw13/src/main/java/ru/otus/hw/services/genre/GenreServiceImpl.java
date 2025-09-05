package ru.otus.hw.services.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {


    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        genreRepository.findAll().forEach(
                genre -> {
                    genres.add(genre);
                }
        );
        return genres;
    }
}
