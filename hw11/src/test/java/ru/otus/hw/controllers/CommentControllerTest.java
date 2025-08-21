package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.CommentController;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.CommentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentMapper commentMapper;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void getCommentsByBookId_ShouldReturnComments() {

        String bookId = "book-1";
        CommentDto comment1 = new CommentDto("1", "Comment_1");
        CommentDto comment2 = new CommentDto("2",  "Comment_2");

        when(commentService.findAllByBookId(bookId)).thenReturn(Flux.just(comment1, comment2));


        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/comments")
                        .queryParam("bookId", bookId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(2)
                .contains(comment1, comment2);
    }

    @Test
    void getCommentsByBookId_WhenNoComments_ShouldReturnEmptyList() {

        String bookId = "book-1";
        when(commentService.findAllByBookId(bookId)).thenReturn(Flux.empty());


        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/comments")
                        .queryParam("bookId", bookId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(0);
    }

    @Test
    void getCommentsByBookId_WhenServiceError_ShouldReturnInternalServerError() {

        String bookId = "book-1";
        when(commentService.findAllByBookId(bookId))
                .thenReturn(Flux.error(new RuntimeException("Database error")));


        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/comments")
                        .queryParam("bookId", bookId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void getCommentsByBookId_WithoutBookIdParam_ShouldReturnBadRequest() {

        webTestClient.get()
                .uri("/api/comments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getCommentsByBookId_ShouldReturnCorrectJsonStructure() {

        String bookId = "book-1";
        CommentDto comment = new CommentDto("1","Comment_1");

        when(commentService.findAllByBookId(bookId)).thenReturn(Flux.just(comment));


        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/comments")
                        .queryParam("bookId", bookId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].text").isEqualTo("Comment_1");

    }
}