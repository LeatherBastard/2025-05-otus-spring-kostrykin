package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BookController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import({CommentMapper.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private CommentMapper commentMapper;

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
    void shouldRenderBooksPage() throws Exception {
        when(bookService.findAll()).thenReturn(books);
        mvc.perform(get("/books"))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", books.stream().map(bookMapper::bookToDto).toList()));
    }

    @Test
    void shouldRenderBookByIdPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        when(commentService.findAllByBookId(1L)).thenReturn(comments);
        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", bookMapper.bookToDto(books.get(0))))
                .andExpect(model().attribute("comments", comments.stream().map(commentMapper::commentToDto)
                        .toList()));
    }

    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        when(commentService.findAllByBookId(1L)).thenReturn(comments);
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        mvc.perform(get("/books/1/edit"))
                .andExpect(view().name("editbook"))
                .andExpect(model().attribute("authors", authors.stream().map(authorMapper::authorToDto).toList()))
                .andExpect(model().attribute("genres", genres.stream().map(genreMapper::genreToDto).toList()))
                .andExpect(model().attribute("comments", comments.stream().map(commentMapper::commentToDto).toList()))
                .andExpect(model().attribute("book", bookMapper.bookToDto(books.get(0))));
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
        Book book = books.get(0);
        CreateBookDto createBookDto = new CreateBookDto(book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));
        mvc.perform(post("/book").flashAttr("bookDto", createBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).insert(any(CreateBookDto.class));
    }


    @Test
    void shouldUpdateBookAndRedirectToContextPath() throws Exception {
        Book book = books.get(0);
        UpdateBookDto updateBookDto = new UpdateBookDto(book.getId(), book.getTitle(), book.getAuthor().getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));
        mvc.perform(put("/books/1/edit").flashAttr("bookDto", updateBookDto))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).update(any(UpdateBookDto.class));
    }

    @Test
    void shouldDeleteBookAndRedirectToContextPath() throws Exception {
        mvc.perform(delete("/books/1"))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(bookService.findById(1L)).thenThrow(new EntityNotFoundException(anyString()));
        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(view().name("customError")).andExpect(status().isNotFound());
    }

    @Test
    void shouldRenderErrorPageWhenWrongRequestParam() throws Exception {
        mvc.perform(get("/books/").param("bookId", "ab"))
                .andExpect(view().name("customError")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRenderErrorPageWhenSomethingGoWrong() throws Exception {
        when(bookService.findAll()).thenThrow(new IllegalArgumentException("Just exception"));
        mvc.perform(get("/books")).andExpect(view().name("customError"))
                .andExpect(status().isInternalServerError());
    }

}
