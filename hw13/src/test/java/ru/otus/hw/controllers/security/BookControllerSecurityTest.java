package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.WebSecurityConfiguration;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.author.AuthorService;
import ru.otus.hw.services.book.BookService;
import ru.otus.hw.services.comment.CommentService;
import ru.otus.hw.services.genre.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import({WebSecurityConfiguration.class, CommentMapper.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
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

    List<Author> authors = List.of(new Author(1L, "Author_1"), new Author(2L, "Author_2"));
    List<Genre> genres = List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"));
    List<Book> books = List.of(
            new Book(1L, "Book1", authors.get(0), genres, null),
            new Book(2L, "Book2", authors.get(1), genres, null)
    );

    List<Comment> comments = List.of(new Comment(1L, books.get(0), "Comment_1"));

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
        Book book = books.get(0);
        CreateBookDto createBookDto = new CreateBookDto(book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));
        mvc.perform(post("/book").with(user("user")).flashAttr("bookDto", createBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).insert(any(CreateBookDto.class));
    }

    @Test
    void shouldNotAddBookWhenUserNotRegistered() throws Exception {
        Book book = books.get(0);
        CreateBookDto createBookDto = new CreateBookDto(book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));
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
        Book book = books.get(0);
        UpdateBookDto updateBookDto = new UpdateBookDto(book.getId(), book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));
        mvc.perform(put("/books/1/edit").with(user("user")).flashAttr("bookDto", updateBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).update(any(UpdateBookDto.class));
    }

    @Test
    void shouldUpdateBookWhenUserNotRegistered() throws Exception {
        Book book = books.get(0);
        UpdateBookDto updateBookDto = new UpdateBookDto(book.getId(), book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));
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


    @Test
    @WithMockUser(username = "user1")
    void shouldNotAccessBookWhenNoReadPermission() throws Exception {
        when(bookService.findById(1L)).thenThrow(new AccessDeniedException("Access Denied"));

        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("customError"));
    }

    @Test
    @WithMockUser(username = "user1")
    void shouldNotUpdateBookWhenNoAdministrationPermission() throws Exception {
        Book book = books.get(0);
        UpdateBookDto updateBookDto = new UpdateBookDto(book.getId(), book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));

        doThrow(new AccessDeniedException("Access Denied"))
                .when(bookService).update(any(UpdateBookDto.class));

        mvc.perform(put("/books/1/edit").flashAttr("bookDto", updateBookDto))
                .andExpect(status().isForbidden())
                .andExpect(view().name("customError"));
    }

    @Test
    @WithMockUser(username = "user1")
    void shouldNotDeleteBookWhenNoAdministrationPermission() throws Exception {
        doThrow(new AccessDeniedException("Access Denied"))
                .when(bookService).deleteById(anyLong());

        mvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("customError"));
    }


    @Test
    @WithMockUser(username = "user1")
    void shouldReturnOnlyPermittedBooks() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(books.get(0)));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "user1")
    void shouldNotRenderEditPageWhenNoReadPermission() throws Exception {
        when(bookService.findById(1L)).thenThrow(new AccessDeniedException("Access Denied"));

        mvc.perform(get("/books/1/edit"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("customError"));
    }

    @Test
    @WithMockUser(username = "user1")
    void shouldHandleAccessDeniedExceptionProperly() throws Exception {
        when(bookService.findById(1L)).thenThrow(new AccessDeniedException("Custom access denied message"));

        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("customError"))
                .andExpect(model().attribute("errorText", "Custom access denied message"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessToAdminRole() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        when(commentService.findAllByBookId(1L)).thenReturn(comments);

        mvc.perform(get("/books/1/edit"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowBookCreationForUserRole() throws Exception {
        Book book = books.get(0);
        CreateBookDto createBookDto = new CreateBookDto(book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));

        mvc.perform(post("/book").flashAttr("bookDto", createBookDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).insert(any(CreateBookDto.class));
    }

    @Test
    @WithMockUser(username = "user1")
    void shouldHandleNotFoundExceptionWithProperSecurity() throws Exception {
        when(bookService.findById(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/books/").param("bookId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("customError"));
    }


}
