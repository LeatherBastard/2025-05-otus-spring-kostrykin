package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.WebSecurityConfiguration;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@Import(WebSecurityConfiguration.class)
public class BookControllerSecurityTest {

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

    List<AuthorDto> authors = List.of(new AuthorDto(1L, "Author_1"), new AuthorDto(2L, "Author_2"));
    List<GenreDto> genres = List.of(new GenreDto(1L, "Genre_1"), new GenreDto(2L, "Genre_2"));
    List<BookDto> books = List.of(
            new BookDto(1L, "Book1", authors.get(0), genres),
            new BookDto(2L, "Book2", authors.get(1), genres)
    );

    List<CommentDto> comments = List.of(new CommentDto(1L, "Comment_1"));

    @Test
    public void testGetBooksOnUser() throws Exception {
        mvc.perform(get("/books").with(user("user"))).andExpect(status().isOk());
    }

    @Test
    public void testGetBooksOnNotRegistered() throws Exception {
        mvc.perform(get("/books")).andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldAddBookAndRedirectToContextPath() throws Exception {
        BookDto book = books.get(0);
        CreateBookDto createBookDto = new CreateBookDto(book.title(), book.author().id(),
                Set.of(genres.get(0).id(), genres.get(1).id()));
        mvc.perform(post("/book").with(user("user")).flashAttr("bookDto", createBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).insert(any(CreateBookDto.class));
    }

    @Test
    void shouldNotAddBookWhenUserNotRegistered() throws Exception {
        BookDto book = books.get(0);
        CreateBookDto createBookDto = new CreateBookDto(book.title(), book.author().id(),
                Set.of(genres.get(0).id(), genres.get(1).id()));
        mvc.perform(post("/book").flashAttr("bookDto", createBookDto))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldRenderBookByIdPageWhenUserRegistered() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        when(commentService.findAllByBookId(1L)).thenReturn(comments);
        mvc.perform(get("/books/").with(user("user")).param("bookId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRenderBookByIdPageWhenUserNotRegistered() throws Exception {
        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    void shouldRenderAddPageWhenUserRegistered() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/book").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRenderAddPageWhenUserNotRegistered() throws Exception {
        mvc.perform(get("/book")).andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldRenderEditPageWhenUserRegistered() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        when(commentService.findAllByBookId(1L)).thenReturn(comments);
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        mvc.perform(get("/books/1/edit").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRenderEditPageWhenUserNotRegistered() throws Exception {
        mvc.perform(get("/books/1/edit")).andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldUpdateBookWhenUserRegistered() throws Exception {
        BookDto book = books.get(0);
        UpdateBookDto updateBookDto = new UpdateBookDto(book.id(), book.title(), book.author().id(),
                Set.of(genres.get(0).id(), genres.get(1).id()));
        mvc.perform(put("/books/1/edit").with(user("user")).flashAttr("bookDto", updateBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).update(any(UpdateBookDto.class));
    }

    @Test
    void shouldUpdateBookWhenUserNotRegistered() throws Exception {
        BookDto book = books.get(0);
        UpdateBookDto updateBookDto = new UpdateBookDto(book.id(), book.title(), book.author().id(),
                Set.of(genres.get(0).id(), genres.get(1).id()));
        mvc.perform(put("/books/1/edit").flashAttr("bookDto", updateBookDto))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(0)).update(any(UpdateBookDto.class));
    }

    @Test
    void shouldDeleteBookWhenUserRegistered() throws Exception {
        mvc.perform(delete("/books/1").with(user("user")))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldNotDeleteBookWhenUserNotRegistered() throws Exception {
        mvc.perform(delete("/books/1"))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(0)).deleteById(anyLong());
    }


}
