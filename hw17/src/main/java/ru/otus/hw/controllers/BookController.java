package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{bookId}")
    public BookDto getBookById(@PathVariable long bookId) {
        return bookService.findById(bookId);
    }

    @PatchMapping("/books/{bookId}")
    public BookDto updateBook(@PathVariable long bookId, @RequestBody @Valid UpdateBookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/books/{bookId}")
    public void deleteBook(@PathVariable long bookId) {
        bookService.deleteById(bookId);
    }


    @PostMapping("/books")
    public BookDto addBook(@RequestBody @Valid CreateBookDto bookDto) {
        return bookService.insert(bookDto);
    }
}
