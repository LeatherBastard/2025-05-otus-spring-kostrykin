package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuthorRepository.class)
class JpaAuthorRepositoryTest {
    private static final long AUTHOR_ID = 3L;
    private static final int EXPECTED_AUTHORS_COUNT = 3;

    @Autowired
    private JpaAuthorRepository authorRepository;

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
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).usingRecursiveComparison().isEqualTo(expectedAuthors);
    }
}
