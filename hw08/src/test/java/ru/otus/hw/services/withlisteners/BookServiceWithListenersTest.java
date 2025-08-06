package ru.otus.hw.services.withlisteners;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.events.MongoBookCascadeDeleteEventListener;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({MongoBookCascadeDeleteEventListener.class, BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class BookServiceWithListenersTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;


    @AfterEach
    void dropDb() {
        bookRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void shouldNotReturnCommentsAfterBookDelete() {
        List<Book> books = new ArrayList<>();
        books.add(bookRepository.save(
                new Book("BookTitle_1",
                        new Author("Author_1"),
                        List.of(new Genre("Genre_1"), new Genre("Genre_2")))));
        books.add(bookRepository.save(
                new Book("BookTitle_2",
                        new Author("Author_2"),
                        List.of(new Genre("Genre_3"), new Genre("Genre_4")))));
        books.add(bookRepository.save(
                new Book("BookTitle_3",
                        new Author("Author_3"),
                        List.of(new Genre("Genre_5"), new Genre("Genre_6")))));

        List<Comment> comments = new ArrayList<>();
        comments.add(commentRepository.save(new Comment(books.get(2), "Comment_1")));
        comments.add(commentRepository.save(new Comment(books.get(2), "Comment_2")));
        bookService.deleteById(books.get(2).getId());
        List<Comment> actualComments = commentRepository.findByBookId(books.get(2).getId());
        assertThat(actualComments.isEmpty());
    }

}
