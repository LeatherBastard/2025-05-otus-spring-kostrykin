package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BookController.class)
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class, CommentMapper.class})
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;


    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private BookRepository bookRepository;

    AuthorDto author1 = new AuthorDto("1", "Author_1");
    AuthorDto author2 = new AuthorDto("2", "Author_2");

    GenreDto genre1 = new GenreDto("1", "Genre_1");
    GenreDto genre2 = new GenreDto("2", "Genre_2");

    BookDto book1 = new BookDto("1", "Book One", author1, List.of(genre1));
    BookDto book2 = new BookDto("2", "Book Two", author2, List.of(genre2));

    @Test
    void getBooks_ShouldReturnAllBooks() {

        when(bookService.findAll()).thenReturn(Flux.just(book1, book2));


        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    void getBooks_WhenNoBooks_ShouldReturnEmptyList() {

        when(bookService.findAll()).thenReturn(Flux.empty());


        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(0);
    }


    @Test
    void getBookById_ShouldReturnBook() {

        when(bookService.findById(book1.id())).thenReturn(Mono.just(book1));


        webTestClient.get()
                .uri("/api/books/{id}", book1.id())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(book1);
    }

    @Test
    void getBookById_WhenNotFound_ShouldReturnNotFound() {

        String bookId = "999";
        when(bookService.findById(bookId))
                .thenReturn(Mono.error(new EntityNotFoundException("Book not found")));

        webTestClient.get()
                .uri("/api/books/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void updateBook_ShouldReturnUpdatedBook() {

        String bookId = "1";
        UpdateBookDto updateDto = new UpdateBookDto(bookId, "Book", "2", Set.of("2"));

        AuthorDto author = new AuthorDto("2", "Author_2");
        GenreDto genre = new GenreDto("2", "Genre_2");
        BookDto updatedBook = new BookDto(bookId, "Book", author, List.of(genre));

        when(bookService.update(any(UpdateBookDto.class))).thenReturn(Mono.just(updatedBook));


        webTestClient.patch()
                .uri("/api/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(updatedBook);
    }

    @Test
    void updateBook_WithInvalidData_ShouldReturnBadRequest() {

        String bookId = "1";
        UpdateBookDto invalidDto = new UpdateBookDto(bookId, "", "1", Set.of());


        webTestClient.patch()
                .uri("/api/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void deleteBook_ShouldReturnOk() {

        String bookId = "1";
        when(bookService.deleteById(bookId)).thenReturn(Mono.empty());


        webTestClient.delete()
                .uri("/api/books/{id}", bookId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteBook_WhenNotFound_ShouldReturnNotFound() {

        String bookId = "999";
        when(bookService.deleteById(bookId))
                .thenReturn(Mono.error(new EntityNotFoundException("Book not found")));


        webTestClient.delete()
                .uri("/api/books/{id}", bookId)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void addBook_ShouldReturnCreatedBook() {

        CreateBookDto createDto = new CreateBookDto("Book", "Genre_1", Set.of("1"));

        AuthorDto author = new AuthorDto("1", "Author_1");
        GenreDto genre = new GenreDto("1", "Genre_1");
        BookDto createdBook = new BookDto("3", "Book", author, List.of(genre));

        when(bookService.insert(any(CreateBookDto.class))).thenReturn(Mono.just(createdBook));


        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(createdBook);
    }

    @Test
    void addBook_WithInvalidData_ShouldReturnBadRequest() {

        CreateBookDto invalidDto = new CreateBookDto("", "1", Set.of());


        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void getBooks_ShouldReturnCorrectJsonStructure() {

        AuthorDto author = new AuthorDto("1", "Author_1");
        GenreDto genre1 = new GenreDto("1", "Genre_1");
        GenreDto genre2 = new GenreDto("2", "Genre_2");
        BookDto book = new BookDto("1", "Book", author, List.of(genre1, genre2));

        when(bookService.findAll()).thenReturn(Flux.just(book));


        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].title").isEqualTo(book.title())
                .jsonPath("$[0].author.id").isEqualTo(book.author().id())
                .jsonPath("$[0].author.fullName").isEqualTo(book.author().fullName())
                .jsonPath("$[0].genres[0].id").isEqualTo(book.genres().get(0).id())
                .jsonPath("$[0].genres[0].name").isEqualTo(book.genres().get(0).name())
                .jsonPath("$[0].genres[1].id").isEqualTo(book.genres().get(1).id())
                .jsonPath("$[0].genres[1].name").isEqualTo(book.genres().get(1).name());
    }

    @Test
    void getBookById_ShouldReturnDetailedJsonStructure() {

        String bookId = "1";
        AuthorDto author = new AuthorDto("1", "Author_1");
        GenreDto genre = new GenreDto("1", "Genre_1");
        BookDto book = new BookDto(bookId, "Book_1", author, List.of(genre));

        when(bookService.findById(bookId)).thenReturn(Mono.just(book));


        webTestClient.get()
                .uri("/api/books/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(book.id())
                .jsonPath("$.title").isEqualTo(book.title())
                .jsonPath("$.author.id").isEqualTo(book.author().id())
                .jsonPath("$.author.fullName").isEqualTo(book.author().fullName())
                .jsonPath("$.genres[0].id").isEqualTo(book.genres().get(0).id())
                .jsonPath("$.genres[0].name").isEqualTo(book.genres().get(0).name());
    }
}