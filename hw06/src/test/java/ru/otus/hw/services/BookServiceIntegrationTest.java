package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.dto.BookDto;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
class BookServiceIntegrationTest {
    private static final long BOOK_ID = 3L;

    @Autowired
    private BookService bookService;

    @Autowired
    private CommentService commentService;

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFields() {
        Optional<BookDto> bookDto = bookService.findById(BOOK_ID);
        assertThat(bookDto.get().comments()).hasSize(2);
        commentService.insert(BOOK_ID, "Good Book");
        bookDto = bookService.findById(BOOK_ID);
        assertThat(bookDto.get().comments()).hasSize(3);
        assertThat(bookDto.get().genres()).hasSizeGreaterThan(0);
    }

}
