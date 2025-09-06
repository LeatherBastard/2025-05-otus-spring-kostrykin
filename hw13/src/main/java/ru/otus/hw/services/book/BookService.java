package ru.otus.hw.services.book;

import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(long id);

    List<Book> findAll();

    Book insert(CreateBookDto bookDto);

    Book update(UpdateBookDto bookDto);

    void deleteById(long id);
}
