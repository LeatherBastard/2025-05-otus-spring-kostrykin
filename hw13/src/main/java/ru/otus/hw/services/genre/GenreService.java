package ru.otus.hw.services.genre;

import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}
