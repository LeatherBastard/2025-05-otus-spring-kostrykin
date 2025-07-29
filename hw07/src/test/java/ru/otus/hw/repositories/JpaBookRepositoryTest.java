package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaBookRepositoryTest {

    private static final long BOOK_ID = 3L;
    private static final long SECOND_BOOK_ID = 2L;
    private static final long FOURTH_BOOK_ID = 4L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    private List<Book> expectedBooks;

    @BeforeEach
    public void initialize() {
        expectedBooks = List.of(
                new Book(1, "BookTitle_1",
                        authorRepository.findById(1L).get(),
                        List.of(new Genre(1, "Genre_1"), new Genre(2, "Genre_2")),
                        null),
                new Book(2, "BookTitle_2",
                        authorRepository.findById(2L).get(),
                        List.of(new Genre(3, "Genre_3"), new Genre(4, "Genre_4")),
                        null),
                new Book(3, "BookTitle_3",
                        authorRepository.findById(3L).get(),
                        List.of(new Genre(5, "Genre_5"), new Genre(6, "Genre_6")),
                        null)
        );
    }

    @Test
    void shouldFindExpectedBookById() {
        Book actualBook = bookRepository.findById(BOOK_ID).get();
        Book expectedBook = em.find(Book.class, BOOK_ID);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @Test
    void shouldReturnCorrectBooksListWithAllInfo() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).usingRecursiveComparison().ignoringFields("comments").isEqualTo(expectedBooks);

    }

    @Test
    void shouldDeleteBookById() {
        Book book2 = em.find(Book.class, SECOND_BOOK_ID);
        assertThat(book2).isNotNull();
        bookRepository.deleteById(SECOND_BOOK_ID);
        em.flush();
        book2 = em.find(Book.class, SECOND_BOOK_ID);
        assertThat(book2).isNull();
    }

    @Test
    void shouldSaveBook() {
        Book book = new Book(0,
                "Book",
                authorRepository.findById(1L).get(),
                List.of(new Genre(1, "Genre_1")),
                new ArrayList<>());
        bookRepository.save(book);
        em.clear();
        Book actualBook = em.find(Book.class, FOURTH_BOOK_ID);
        assertThat(actualBook).usingRecursiveComparison().ignoringFields("id", "author", "genres").isEqualTo(book);
    }


}
