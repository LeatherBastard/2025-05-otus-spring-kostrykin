package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({JpaCommentRepository.class, JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class, CommentConverter.class, BookConverter.class, AuthorConverter.class, GenreConverter.class, BookServiceImpl.class})
@RequiredArgsConstructor
class BookServiceIntegrationTest {
    private static final long BOOK_ID = 3L;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookConverter bookConverter;

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindById() {
        BookDto bookDto = bookService.findById(BOOK_ID).get();
        assertDoesNotThrow(() -> bookConverter.bookToString(bookDto));

    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindAll() {
        List<BookDto> books = bookService.findAll();
        assertDoesNotThrow(() -> books.stream().map(bookConverter::bookToString));

    }

}
