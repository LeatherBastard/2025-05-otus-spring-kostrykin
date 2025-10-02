package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.dto.genre.GenreBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;


    List<Author> authors = List.of(new Author(1, "Author_1"), new Author(2, "Author_2"));
    List<GenreBookDto> genres = List.of(new GenreBookDto(1, "Genre_1"), new GenreBookDto(2, "Genre_2"));
    List<BookDto> books = List.of(
            new BookDto(1, "Book1", authors.get(0), genres),
            new BookDto(2, "Book2", authors.get(1), genres)
    );

    UpdateBookDto updateBookDto = new UpdateBookDto(1L, "Book_1", 2L, Set.of(1L, 2L, 3L));
    CreateBookDto createBookDto = new CreateBookDto("Book_1", 2L, Set.of(1L, 2L, 3L));
    List<Comment> comments = List.of();

    @Test
    void shouldReturnBooks() throws Exception {
        when(bookService.findAll()).thenReturn(books);
        mvc.perform(get("/api/books")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        when(bookService.findById(1)).thenReturn(books.get(0));
        mvc.perform(get("/api/books/1")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books.get(0))));
    }

    @Test
    void shouldDeleteBookById() throws Exception {
        mvc.perform(delete("/api/books/1")).andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(1);
    }

    @Test
    void shouldUpdateBookById() throws Exception {
        mvc.perform(patch("/api/books/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDto)))
                .andExpect(status().isOk());
        verify(bookService, times(1)).update(updateBookDto);
    }


    @Test
    void shouldAddBook() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDto)))
                .andExpect(status().isOk());
        verify(bookService, times(1)).insert(createBookDto);
    }


    @Test
    void shouldReturnErrorWhenFindBookById() throws Exception {
        when(bookService.findById(5)).thenThrow(new EntityNotFoundException(""));
        mvc.perform(get("/api/books/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void shouldReturnErrorWhenAddBook() throws Exception {
        CreateBookDto bookDto = new CreateBookDto("", createBookDto.authorId(), null);
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void shouldReturnErrorWhenUpdateBookById() throws Exception {
        UpdateBookDto bookDto = new UpdateBookDto(null, "", updateBookDto.authorId(), updateBookDto.genreIds());
        mvc.perform(patch("/api/books/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    void shouldReturnInternalServerError() throws Exception {
        when(bookService.findById(5)).thenThrow(new IllegalArgumentException("s"));
        mvc.perform(get("/api/books/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}
