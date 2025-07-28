package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaCommentRepository.class, JpaBookRepository.class})
class JpaCommentRepositoryTest {
    private static final long COMMENT_ID = 3L;
    private static final long BOOK_ID = 3L;


    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private List<Comment> expectedComments;

    @BeforeEach
    public void initialize() {
        expectedComments = List.of(
                new Comment(3, bookRepository.findById(3).get(), "Very good"),
                new Comment(4, bookRepository.findById(3).get(), "Boring"));
    }

    @Test
    void shouldFindExpectedCommentById() {
        Comment actualComment = commentRepository.findById(COMMENT_ID).get();
        Comment expectedComment = em.find(Comment.class, COMMENT_ID);
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    void shouldReturnCorrectCommentsByBookIdListWithAllInfo() {
        List<Comment> comments = commentRepository.findAllByBookId(BOOK_ID);
        assertThat(comments).usingRecursiveComparison().isEqualTo(expectedComments);
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
        comment = commentRepository.save(comment);
        em.clear();
        Comment actualComment = em.find(Comment.class, comment.getId());
        assertThat(actualComment).usingRecursiveComparison().ignoringFields("id", "book").isEqualTo(comment);
    }

}
