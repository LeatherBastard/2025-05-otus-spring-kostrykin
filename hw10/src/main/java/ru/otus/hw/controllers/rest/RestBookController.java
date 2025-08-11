package ru.otus.hw.controllers.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestBookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{bookId}")
    public BookDto getBookById(@PathVariable long bookId) {
        return bookService.findById(bookId);
    }
}
