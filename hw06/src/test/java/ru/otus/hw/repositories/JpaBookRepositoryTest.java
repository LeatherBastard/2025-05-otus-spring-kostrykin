package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class, JpaAuthorRepository.class})
@ActiveProfiles("test")
class JpaBookRepositoryTest {

    private static final long BOOK_ID = 3L;
    private static final long SECOND_BOOK_ID = 2L;
    private static final long FOURTH_BOOK_ID = 4L;
    private static final int EXPECTED_BOOKS_COUNT = 3;

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private JpaGenreRepository genreRepository;

    @Autowired
    private JpaAuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindExpectedBookById() {
        Book actualBook = bookRepository.findById(BOOK_ID);
        Book expectedBook = em.find(Book.class, BOOK_ID);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @Test
    void shouldReturnCorrectBooksListWithAllInfo() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT)
                .allMatch(s -> !s.getTitle().equals(""))
                .allMatch(s -> s.getAuthor() != null)
                .allMatch(s -> s.getGenres() != null && s.getGenres().size() > 0)
                .allMatch(s -> s.getComments() != null && s.getComments().size() > 0);
    }

    @Test
    void shouldDeleteBookById() {
        Book book2 = em.find(Book.class, SECOND_BOOK_ID);
        assertThat(book2).isNotNull();
        bookRepository.deleteById(SECOND_BOOK_ID);
        em.clear();
        book2 = em.find(Book.class, SECOND_BOOK_ID);
        assertThat(book2).isNull();
    }

    @Test
    void shouldSaveBook() {
        Book book = new Book(0,
                "Book",
                authorRepository.findById(1),
                genreRepository.findAll(),
                new ArrayList<>());
        bookRepository.save(book);
        em.clear();
        Book actualBook = em.find(Book.class, FOURTH_BOOK_ID);
        assertThat(actualBook).isNotNull().hasFieldOrPropertyWithValue("title", book.getTitle());
    }


}
