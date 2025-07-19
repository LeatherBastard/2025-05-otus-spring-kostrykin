package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ActiveProfiles("test")
@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
@RequiredArgsConstructor
class CommentServiceIntegrationTest {

    private static final long BOOK_ID = 3L;

    private static final long COMMENT_ID = 1L;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentConverter commentConverter;

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindById() {
        CommentDto commentDto = commentService.findById(COMMENT_ID).get();
        assertDoesNotThrow(() -> commentConverter.commentToString(commentDto));
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindAllByBookId() {
        List<CommentDto> books = commentService.findAllByBookId(BOOK_ID);
        assertDoesNotThrow(() -> books.stream().map(commentConverter::commentToString));

    }


}
