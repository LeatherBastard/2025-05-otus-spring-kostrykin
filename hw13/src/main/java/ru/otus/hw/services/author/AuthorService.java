package ru.otus.hw.services.author;

import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
