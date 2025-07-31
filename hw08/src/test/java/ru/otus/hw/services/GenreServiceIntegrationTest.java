package ru.otus.hw.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({GenreServiceImpl.class, GenreConverter.class})
class GenreServiceIntegrationTest {
    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreConverter genreConverter;



    @AfterEach
    void dropDb() {
        genreRepository.deleteAll();
    }

    @Test
    void shouldFindAllGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(genreRepository.save(new Genre("Genre_1")));
        genres.add(genreRepository.save(new Genre("Genre_2")));
        genres.add(genreRepository.save(new Genre("Genre_3")));
        genres.add(genreRepository.save(new Genre("Genre_4")));
        genres.add(genreRepository.save(new Genre("Genre_5")));
        genres.add(genreRepository.save(new Genre("Genre_6")));

        List<GenreDto> genreDtos = genreService.findAll();
        assertThat(genreDtos).usingRecursiveComparison()
                .isEqualTo(genres.stream().map(genreConverter::genreToDto).toList());
    }
}
