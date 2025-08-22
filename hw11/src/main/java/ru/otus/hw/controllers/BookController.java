package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public Flux<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{bookId}")
    public Mono<BookDto> getBookById(@PathVariable String bookId) {
        return bookService.findById(bookId);
    }

    @PatchMapping("/books/{bookId}")
    public Mono<BookDto> updateBook(@PathVariable String bookId, @RequestBody @Valid UpdateBookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/books/{bookId}")
    public Mono<Void> deleteBook(@PathVariable String bookId) {
        return bookService.deleteById(bookId)
                .onErrorResume(EntityNotFoundException.class, e ->
                        Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(bookId))
                        ));

    }

    @PostMapping("/books")
    public Mono<BookDto> addBook(@RequestBody @Valid CreateBookDto bookDto) {
        return bookService.insert(bookDto);
    }
}
