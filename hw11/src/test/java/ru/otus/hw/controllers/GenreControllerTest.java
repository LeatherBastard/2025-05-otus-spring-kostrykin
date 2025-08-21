package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.GenreService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;


    @MockBean
    private GenreMapper genreMapper;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void getGenres_ShouldReturnAllGenres() {

        GenreDto genre1 = new GenreDto("1", "Genre_1");
        GenreDto genre2 = new GenreDto("2", "Genre_2");
        GenreDto genre3 = new GenreDto("3", "Genre_3");

        when(genreService.findAll()).thenReturn(Flux.just(genre1, genre2, genre3));


        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(3)
                .contains(genre1, genre2, genre3);
    }

    @Test
    void getGenres_WhenNoGenres_ShouldReturnEmptyList() {

        when(genreService.findAll()).thenReturn(Flux.empty());


        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(0);
    }

    @Test
    void getGenres_WhenServiceError_ShouldReturnInternalServerError() {

        when(genreService.findAll())
                .thenReturn(Flux.error(new RuntimeException("Database connection failed")));


        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getGenres_ShouldReturnCorrectJsonStructure() {

        GenreDto genre = new GenreDto("1", "Genre_1");

        when(genreService.findAll()).thenReturn(Flux.just(genre));


        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].name").isEqualTo("Genre_1");

    }

    @Test
    void getGenres_ShouldReturnCorrectContentType() {

        GenreDto genre = new GenreDto("1", "Genre_1");

        when(genreService.findAll()).thenReturn(Flux.just(genre));


        webTestClient.get()
                .uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void getGenres_WithMultipleGenres_ShouldReturnCorrectOrder() {

        GenreDto genre1 = new GenreDto("1", "Genre_1");
        GenreDto genre2 = new GenreDto("2", "Genre_2");
        GenreDto genre3 = new GenreDto("3", "Genre_3");

        when(genreService.findAll()).thenReturn(Flux.just(genre1, genre2, genre3));


        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(3)
                .value(genres -> {
                    assertThat(genres.get(0).id()).isEqualTo("1");
                    assertThat(genres.get(1).id()).isEqualTo("2");
                    assertThat(genres.get(2).id()).isEqualTo("3");
                });
    }
}
