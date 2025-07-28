package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({JpaCommentRepository.class, JpaBookRepository.class, CommentConverter.class, CommentServiceImpl.class})
@RequiredArgsConstructor
class CommentServiceIntegrationTest {

    private static final long BOOK_ID = 3L;

    private static final long COMMENT_ID = 3L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentConverter commentConverter;

    private List<Comment> expectedComments;


    @BeforeEach
    public void initialize() {
        expectedComments = List.of(
                new Comment(3, null, "Very good"),
                new Comment(4, null, "Boring"));
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindById() {
        CommentDto commentDto = commentService.findById(COMMENT_ID).get();
        CommentDto expectedCommentDto = commentConverter.commentToDto(expectedComments.get(0));
        assertThat(commentDto).usingRecursiveComparison().isEqualTo(expectedCommentDto);
        assertDoesNotThrow(() -> commentConverter.commentToString(commentDto));
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindAllByBookId() {
        List<CommentDto> commentDtos = commentService.findAllByBookId(BOOK_ID);
        List<CommentDto> expectedCommentDtos = expectedComments.stream().map(commentConverter::commentToDto).toList();
        assertThat(commentDtos).usingRecursiveComparison().isEqualTo(expectedCommentDtos);
        assertDoesNotThrow(() -> commentDtos.stream().map(commentConverter::commentToString));

    }


}
