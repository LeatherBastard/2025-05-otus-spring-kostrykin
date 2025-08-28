package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.AuthorService;

import static org.mockito.Mockito.when;


@WebFluxTest(controllers = AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorMapper authorMapper;

    @MockBean
    private AuthorRepository authorRepository;


    @Test
    public void testGetAuthors_ShouldReturnAllAuthors() {

        AuthorDto author1 = new AuthorDto("1", "Author_1");
        AuthorDto author2 = new AuthorDto("2", "Author_2");

        when(authorService.findAll()).thenReturn(Flux.just(author1, author2));


        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(2)
                .contains(author1, author2);
    }

    @Test
    public void testGetAuthors_WhenNoAuthors_ShouldReturnEmptyList() {

        when(authorService.findAll()).thenReturn(Flux.empty());


        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(0);
    }

    @Test
    public void testGetAuthors_WhenServiceThrowsException_ShouldReturnError() {

        when(authorService.findAll()).thenReturn(Flux.error(new RuntimeException("Database error")));


        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void testGetAuthors_ShouldReturnCorrectJsonStructure() {

        AuthorDto author = new AuthorDto("1", "Author_1");
        when(authorService.findAll()).thenReturn(Flux.just(author));


        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].fullName").isEqualTo("Author_1");
    }
}