package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.id(), genre.name());
    }

    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
