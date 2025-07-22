package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class JpaGenreRepositoryTest {
    private static final int EXPECTED_GENRES_COUNT = 6;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnCorrectGenresListWithAllInfo() {
        List<Genre> books = genreRepository.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .allMatch(s -> !s.getName().equals(""));
    }

    @Test
    void shouldFindExpectedGenresByIds() {
        List<Genre> books = genreRepository.findAllByIdIn(Set.of(2L, 4L));
        assertThat(books).isNotNull().hasSize(2)
                .allMatch(s -> !s.getName().equals(""));
    }

}
