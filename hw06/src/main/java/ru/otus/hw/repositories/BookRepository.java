package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

import java.util.List;

public interface BookRepository {
    Book findById(long id);

    List<Book> findAll();

    Book save(Book book);

    void deleteById(long id);
}
