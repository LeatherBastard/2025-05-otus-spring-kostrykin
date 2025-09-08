package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.book.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
@WithUserDetails
class BookServiceIntegrationTest {
    private static final long BOOK_ID = 3L;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    private List<Book> expectedBooks;

    @BeforeEach
    public void initialize() {
        expectedBooks = bookService.findAll();

    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindById() {
        Book bookDto = bookService.findById(BOOK_ID).get();
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(expectedBooks.get(2));
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindAll() {
        List<Book> bookDtos = bookService.findAll();
        assertThat(bookDtos).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @Test
    void shouldDeleteById() {
        Book bookDto = bookService.insert(new CreateBookDto("Book", 2L, Set.of(2L, 5L)));
        bookService.deleteById(bookDto.getId());
        Optional<Book> expectedBookDto = bookService.findById(bookDto.getId());
        assertThat(expectedBookDto.isEmpty());
    }

    @Nested
    class InsertTests {
        @Test
        void shouldInsert() {
            Book bookDto = bookService.insert(new CreateBookDto("Book", 2L, Set.of(2L, 5L)));
            Book expectedBookDto = bookService.findById(bookDto.getId()).get();
            assertThat(bookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);
            bookService.deleteById(bookDto.getId());
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenGenresAreEmpty() {
            assertThatThrownBy(() -> bookService.insert(new CreateBookDto("Book", 1L, Set.of())))
                    .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("Genres ids must not be null");
        }

        @Test
        void shouldThrowEntityNotFoundExceptionWhenAuthorIsEmpty() {
            long authorId = 0;
            assertThatThrownBy(() -> bookService.insert(new CreateBookDto("Book", authorId, Set.of(2L, 3L))))
                    .isExactlyInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Author with id %d not found", authorId));
        }

        @Test
        void shouldThrowEntityNotFoundExceptionWhenGenresNotFound() {
            Set genreIds = Set.of(10L, 15L);
            assertThatThrownBy(() -> bookService.insert(new CreateBookDto("Book", 2L, genreIds)))
                    .isExactlyInstanceOf(EntityNotFoundException.class)
                    .hasMessage("One or all genres with ids %s not found", genreIds.toString());
        }

    }

    @Nested
    class UpdateTests {
        @Test
        void shouldUpdate() {
            Book bookDto = bookService.insert(new CreateBookDto("Book", 2L, Set.of(2L, 5L)));
            String updatedTitle = "UpdatedBook";
            long updatedAuthorId = 3L;
            Set<Long> updatedGenreIds = Set.of(5L, 6L);
            Book actualBookDto = bookService.update(new UpdateBookDto(bookDto.getId(), updatedTitle
                    , updatedAuthorId, updatedGenreIds));
            BookDto expectedBookDto = bookMapper.bookToDto(new Book(actualBookDto.getId(),
                    updatedTitle,
                    authorRepository.findById(updatedAuthorId).get(),
                    List.of(new Genre(5, "Genre_5"), new Genre(6, "Genre_6")),
                    null));
            assertThat(actualBookDto)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBookDto);
            bookService.deleteById(actualBookDto.getId());
        }

        @Test
        void shouldThrowEntityNotFoundExceptionWhenBookIsEmpty() {
            long bookId = 0;
            assertThatThrownBy(() -> bookService.update(new UpdateBookDto(bookId, "Book",
                    1L, Set.of(2L, 3L))))
                    .isExactlyInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Book with id %d not found", bookId));
        }

        @Test
        void shouldThrowEntityNotFoundExceptionWhenAuthorIsEmpty() {
            long authorId = 0;
            assertThatThrownBy(() -> bookService.update(new UpdateBookDto(1L, "Book", authorId, Set.of(2L, 3L))))
                    .isExactlyInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Author with id %d not found", authorId));
        }

        @Test
        void shouldThrowEntityNotFoundExceptionWhenGenresAreEmpty() {
            assertThatThrownBy(() -> bookService.update(new UpdateBookDto(1L, "Book", 1L, Set.of())))
                    .isExactlyInstanceOf(EntityNotFoundException.class)
                    .hasMessage("One or all genres with ids [] not found");
        }
    }


}
