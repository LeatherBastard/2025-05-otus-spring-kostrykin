package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {
    private static final int EXPECTED_GENRES_COUNT = 6;

    @Autowired
    private JpaGenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    private List<Genre> expectedGenres;

    @BeforeEach
    public void initialize() {
        expectedGenres = List.of(
                new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2"),
                new Genre(3, "Genre_3"),
                new Genre(4, "Genre_4"),
                new Genre(5, "Genre_5"),
                new Genre(6, "Genre_6")
        );
    }

    @Test
    void shouldReturnCorrectGenresListWithAllInfo() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @Test
    void shouldFindExpectedGenresByIds() {
        List<Genre> genres = List.of(expectedGenres.get(1), expectedGenres.get(3));
        List<Genre> actualGenres = genreRepository.findAllByIds(Set.of(2L, 4L));
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(genres);
    }

}
