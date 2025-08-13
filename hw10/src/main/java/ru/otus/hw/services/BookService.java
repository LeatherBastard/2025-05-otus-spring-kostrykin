package ru.otus.hw.services;

import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(CreateBookDto bookDto);

    BookDto update(UpdateBookDto bookDto);

    void deleteById(long id);
}
