package ru.otus.hw.dto.book;

import ru.otus.hw.dto.genre.GenreBookDto;
import ru.otus.hw.models.Author;

import java.util.List;

public record BookDto(long id, String title, Author author, List<GenreBookDto> genres) {
}
