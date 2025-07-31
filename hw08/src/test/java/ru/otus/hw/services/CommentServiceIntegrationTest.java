package ru.otus.hw.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({CommentServiceImpl.class})
public class CommentServiceIntegrationTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentConverter commentConverter;

    @AfterEach
    void dropDb() {
        commentRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void shouldFindCommentById() {
        Book book = new Book("BookTitle_1",
                new Author("Author_1"),
                List.of(new Genre("Genre_1"), new Genre("Genre_2")));
        book = bookRepository.save(book);
        Comment expectedComment = new Comment(book, "Comment_1");
        expectedComment = commentRepository.save(expectedComment);
        CommentDto actualComment = commentService.findById(expectedComment.getId()).get();
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(commentConverter.commentToDto(expectedComment));
    }

    @Test
    void shouldDeleteCommentById() {
        Book book = new Book("BookTitle_1",
                new Author("Author_1"),
                List.of(new Genre("Genre_1"), new Genre("Genre_2")));
        book = bookRepository.save(book);
        Comment comment = new Comment(book, "Comment_1");
        comment = commentRepository.save(comment);
        commentService.deleteById(comment.getId());
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }
}
