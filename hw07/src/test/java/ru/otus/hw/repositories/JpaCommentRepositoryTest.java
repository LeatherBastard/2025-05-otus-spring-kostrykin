package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class JpaCommentRepositoryTest {
    private static final long COMMENT_ID = 3L;
    private static final long BOOK_ID = 3L;
    private static final long FIFTH_COMMENT_ID = 5L;
    private static final int EXPECTED_COMMENTS_COUNT = 2;


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindExpectedCommentById() {
        Comment actualComment = commentRepository.findById(COMMENT_ID).get();
        Comment expectedComment = em.find(Comment.class, COMMENT_ID);
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    void shouldReturnCorrectCommentsByBookIdListWithAllInfo() {
        List<Comment> comments = commentRepository.findAllByBookId(BOOK_ID);
        assertThat(comments).isNotNull().hasSize(EXPECTED_COMMENTS_COUNT)
                .allMatch(s -> s.getBook() != null)
                .allMatch(s -> s.getText() != null);
    }

    @Test
    void shouldDeleteCommentById() {
        Comment comment = em.find(Comment.class, COMMENT_ID);
        assertThat(comment).isNotNull();
        commentRepository.deleteById(COMMENT_ID);
        em.flush();
        comment = em.find(Comment.class, COMMENT_ID);
        assertThat(comment).isNull();
    }

    @Test
    void shouldSaveComment() {
        Comment comment = new Comment(0, bookRepository.findById(1).get(), "Some comment");
        commentRepository.save(comment);
        em.clear();
        Comment actualComment = em.find(Comment.class, FIFTH_COMMENT_ID);
        assertThat(actualComment).isNotNull().hasFieldOrPropertyWithValue("text", comment.getText());
    }

}
