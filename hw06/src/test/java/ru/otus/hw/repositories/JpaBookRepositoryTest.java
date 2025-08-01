package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class, JpaAuthorRepository.class})
class JpaBookRepositoryTest {

    private static final long BOOK_ID = 3L;
    private static final long SECOND_BOOK_ID = 2L;
    private static final long FOURTH_BOOK_ID = 4L;

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private JpaGenreRepository genreRepository;

    @Autowired
    private JpaAuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    private List<Book> expectedBooks;

    @BeforeEach
    public void initialize() {
        expectedBooks = List.of(
                new Book(1, "BookTitle_1",
                        authorRepository.findById(1).get(),
                        genreRepository.findAllByIds(Set.of(1L, 2L)),
                        null),
                new Book(2, "BookTitle_2",
                        authorRepository.findById(2).get(),
                        genreRepository.findAllByIds(Set.of(3L, 4L)),
                        null),
                new Book(3, "BookTitle_3",
                        authorRepository.findById(3).get(),
                        genreRepository.findAllByIds(Set.of(5L, 6L)),
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
                authorRepository.findById(1).get(),
                genreRepository.findAll(),
                new ArrayList<>());
        bookRepository.save(book);
        em.clear();
        Book actualBook = em.find(Book.class, FOURTH_BOOK_ID);
        assertThat(actualBook).usingRecursiveComparison().ignoringFields("id", "author", "genres").isEqualTo(book);
    }


}
