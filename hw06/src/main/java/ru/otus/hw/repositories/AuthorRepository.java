package ru.otus.hw.repositories;

import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorRepository {
    List<Author> findAll();

    Author findById(long id);
}
