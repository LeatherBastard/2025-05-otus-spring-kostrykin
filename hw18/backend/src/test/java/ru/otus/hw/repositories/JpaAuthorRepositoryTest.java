package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaAuthorRepositoryTest {
    private static final long AUTHOR_ID = 3L;
    private static final int EXPECTED_AUTHORS_COUNT = 3;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    private List<Author> expectedAuthors;

    @BeforeEach
    public void initialize() {
        expectedAuthors = List.of(
                new Author(1, "Author_1"),
                new Author(2, "Author_2"),
                new Author(3, "Author_3")
        );
    }

    @Test
    void shouldFindExpectedAuthorById() {
        Author actualAuthor = authorRepository.findById(AUTHOR_ID).get();
        Author expectedAuthor = em.find(Author.class, AUTHOR_ID);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);

    }

    @Test
    void shouldReturnCorrectBooksListWithAllInfo() {
        List<Author> authors = new ArrayList<>();
        authorRepository.findAll().forEach(authors::add);
        assertThat(authors).usingRecursiveComparison().isEqualTo(expectedAuthors);
    }
}
