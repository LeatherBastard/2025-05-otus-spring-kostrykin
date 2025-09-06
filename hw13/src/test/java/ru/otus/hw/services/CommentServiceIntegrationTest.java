package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CreateCommentDto;
import ru.otus.hw.dto.comment.UpdateCommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.comment.CommentService;
import ru.otus.hw.services.comment.CommentServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({CommentMapper.class, CommentServiceImpl.class})
@RequiredArgsConstructor
@Disabled
class CommentServiceIntegrationTest {

    private static final long BOOK_ID = 3L;

    private static final long COMMENT_ID = 3L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    private List<Comment> expectedComments;


    @BeforeEach
    public void initialize() {
        expectedComments = List.of(
                new Comment(2, null, "Very good"),
                new Comment(3, null, "Boring"));
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindById() {
        Comment commentDto = commentService.findById(COMMENT_ID);
        CommentDto expectedCommentDto = commentMapper.commentToDto(expectedComments.get(1));
        assertThat(commentDto).usingRecursiveComparison().isEqualTo(expectedCommentDto);
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindAllByBookId() {
        List<Comment> commentDtos = commentService.findAllByBookId(BOOK_ID);
        List<CommentDto> expectedCommentDtos = expectedComments.stream().map(commentMapper::commentToDto).toList();
        assertThat(commentDtos).usingRecursiveComparison().isEqualTo(expectedCommentDtos);
    }

    @Test
    void shouldDeleteById() {
        Comment commentDto = commentService.insert(BOOK_ID, new CreateCommentDto("Comment to be deleted"));
        commentService.deleteById(commentDto.getId());
        Comment expectedCommentDto = commentService.findById(commentDto.getId());
        assertThat(expectedCommentDto).isNull();
    }


    @Nested
    class InsertTests {
        @Test
        void shouldInsert() {
            Comment commentDto = commentService.insert(BOOK_ID, new CreateCommentDto("Comment to be inserted"));
            Comment expectedCommentDto = commentService.findById(commentDto.getId());
            assertThat(commentDto).usingRecursiveComparison().isEqualTo(expectedCommentDto);
            commentService.deleteById(commentDto.getId());
        }

        @Test
        void shouldThrowEntityNotFoundExceptionWhenBookIsEmpty() {
            long bookId = 0;
            assertThatThrownBy(() -> commentService.insert(bookId, new CreateCommentDto("Some comment")))
                    .isExactlyInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Book with id %d not found", bookId));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void shouldUpdate() {
            Comment commentDto = commentService.insert(BOOK_ID, new CreateCommentDto("Comment to be updated"));
            String updatedText = "UpdatedComment";
            Comment actualCommentDto = commentService.update(new UpdateCommentDto(commentDto.getId(), updatedText));
            CommentDto expectedCommentDto = commentMapper.commentToDto(new Comment(commentDto.getId(),
                    bookRepository.findById(BOOK_ID).get(),
                    updatedText));
            assertThat(actualCommentDto)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedCommentDto);
            commentService.deleteById(actualCommentDto.getId());
        }

        @Test
        void shouldThrowEntityNotFoundExceptionWhenCommentIsEmpty() {
            long commentId = 0;
            assertThatThrownBy(() -> commentService.update(new UpdateCommentDto(commentId, "Some comment")))
                    .isExactlyInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Comment with id %d not found", commentId));
        }
    }
}
