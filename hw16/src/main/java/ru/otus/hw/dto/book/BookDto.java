package ru.otus.hw.dto.book;

import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.List;

public record BookDto(long id, String title, Author author, List<Genre> genres) {
}
