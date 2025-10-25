package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.clients.BookClient;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;

import java.util.List;

@RequiredArgsConstructor
@Service
@Retry(name = "uiRetryService")
@CircuitBreaker(name = "uiCircuitBreakerService")
public class BookServiceImpl implements BookService {

    private final BookClient bookClient;

    @Override
    public BookDto findById(long id) {
        return bookClient.getBookById(id);
    }

    @Override
    public List<BookDto> findAll() {
        return bookClient.getBooks();
    }

    @Override
    public BookDto insert(CreateBookDto bookDto) {
        return bookClient.addBook(bookDto);
    }

    @Override
    public BookDto update(long id, UpdateBookDto bookDto) {
        return bookClient.updateBook(id, bookDto);
    }

    @Override
    public void deleteById(long id) {
        bookClient.deleteBook(id);
    }


}
