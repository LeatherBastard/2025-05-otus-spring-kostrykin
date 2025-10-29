package ru.otus.hw.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;

import java.util.List;

@FeignClient(name = "book-client", url = "http://localhost:8080/api")
public interface BookClient {

    @GetMapping("/books")
    List<BookDto> getBooks();

    @GetMapping("/books/{bookId}")
    BookDto getBookById(@PathVariable long bookId);

    @PatchMapping("/books/{bookId}")
    BookDto updateBook(@PathVariable long bookId, @RequestBody UpdateBookDto bookDto);

    @DeleteMapping("/books/{bookId}")
    void deleteBook(@PathVariable long bookId);

    @PostMapping("/books")
    BookDto addBook(@RequestBody CreateBookDto bookDto);
}
