package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.genre.GenreBookDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper {

    public GenreBookDto genreToDto(Genre genre) {
        return new GenreBookDto(genre.getId(), genre.getName());
    }
}
