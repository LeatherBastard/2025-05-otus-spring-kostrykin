package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaGenreRepositoryTest {
    private static final int EXPECTED_GENRES_COUNT = 6;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    private List<Genre> expectedGenres;

    @BeforeEach
    public void initialize() {
        expectedGenres = List.of(
                new Genre(1L, "Genre_1"),
                new Genre(2L, "Genre_2"),
                new Genre(3L, "Genre_3"),
                new Genre(4L, "Genre_4"),
                new Genre(5L, "Genre_5"),
                new Genre(6L, "Genre_6")
        );
    }

    @Test
    void shouldReturnCorrectGenresListWithAllInfo() {
        List<Genre> genres = new ArrayList<>();
        genreRepository.findAll().forEach(genres::add);
        assertThat(genres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @Test
    void shouldFindExpectedGenresByIds() {
        List<Genre> genres = List.of(expectedGenres.get(1), expectedGenres.get(3));
        List<Genre> actualGenres = new ArrayList<>();
        genreRepository.findAllById(Set.of(2L, 4L)).forEach(actualGenres::add);
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(genres);
    }

}
