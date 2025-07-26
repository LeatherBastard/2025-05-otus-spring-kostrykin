package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({JpaCommentRepository.class, JpaBookRepository.class, CommentConverter.class, CommentServiceImpl.class})
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
