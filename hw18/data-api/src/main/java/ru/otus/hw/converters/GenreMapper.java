package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper {

    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
