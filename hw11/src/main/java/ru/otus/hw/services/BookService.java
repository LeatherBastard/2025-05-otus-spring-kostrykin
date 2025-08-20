package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;

public interface BookService {
    Mono<BookDto> findById(Long id);

    Flux<BookDto> findAll();

    Mono<BookDto> insert(CreateBookDto bookDto);

    Mono<BookDto> update(UpdateBookDto bookDto);

    void deleteById(Long id);
}
