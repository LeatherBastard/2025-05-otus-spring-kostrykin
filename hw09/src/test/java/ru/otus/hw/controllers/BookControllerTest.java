package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CreateBookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    List<AuthorDto> authors = List.of(new AuthorDto(1, "Author_1"), new AuthorDto(2, "Author_2"));
    List<GenreDto> genres = List.of(new GenreDto(1, "Genre_1"), new GenreDto(2, "Genre_2"));
    List<BookDto> books = List.of(
            new BookDto(1, "Book1", authors.get(0), genres),
            new BookDto(2, "Book2", authors.get(1), genres)
    );

    List<CommentDto> comments = List.of(new CommentDto(1, "Comment_1"));

    @Test
    void shouldRenderBooksPage() throws Exception {
        when(bookService.findAll()).thenReturn(books);
        mvc.perform(get("/books"))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void shouldRenderBookByIdPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        when(commentService.findAllByBookId(1L)).thenReturn(comments);
        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", books.get(0)))
                .andExpect(model().attribute("comments", comments));
    }

    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        when(commentService.findAllByBookId(1L)).thenReturn(comments);
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        mvc.perform(get("/books/1/edit"))
                .andExpect(view().name("editbook"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attribute("book", books.get(0)));
    }

    @Test
    void shouldRenderAddPageWithCorrectViewAndModelAttributes() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/book"))
                .andExpect(view().name("addbook"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @Test
    void shouldAddBookAndRedirectToContextPath() throws Exception {
        BookDto book = books.get(0);
        CreateBookDto createBookDto = new CreateBookDto(book.title(), book.author().id(),
                Set.of(genres.get(0).id(), genres.get(1).id()));
        mvc.perform(post("/book").flashAttr("bookDto", createBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).insert(anyString(), anyLong(), anySet());
    }

    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(bookService.findById(1L)).thenThrow(new EntityNotFoundException(anyString()));
        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(view().name("customError"));
    }


    @Test
    void shouldUpdateBookAndRedirectToContextPath() throws Exception {
        BookDto book = books.get(0);
        UpdateBookDto updateBookDto = new UpdateBookDto(book.id(), book.title(), book.author().id(),
                Set.of(genres.get(0).id(), genres.get(1).id()));
        mvc.perform(put("/books/1/edit").flashAttr("bookDto", updateBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).update(anyLong(), anyString(), anyLong(), anySet());
    }

    @Test
    void shouldDeleteBookAndRedirectToContextPath() throws Exception {
        mvc.perform(delete("/books/1"))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteById(anyLong());
    }

}
