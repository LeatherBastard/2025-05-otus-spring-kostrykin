package ru.otus.hw.services.book;

import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(CreateBookDto bookDto);

    BookDto update(UpdateBookDto bookDto);

    void deleteById(long id);
}
