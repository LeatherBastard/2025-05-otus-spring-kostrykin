package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.clients.BookClient;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
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
        try {
            return bookClient.updateBook(id, bookDto);
        } catch (Exception e) {
            log.info(e.getClass() + " " + e.getCause() + " " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteById(long id) {
        bookClient.deleteBook(id);
    }


}
