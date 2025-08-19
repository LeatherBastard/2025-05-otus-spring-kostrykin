package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({CommentMapper.class, BookMapper.class, AuthorMapper.class, GenreMapper.class, BookServiceImpl.class})
@RequiredArgsConstructor
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
        expectedBooks = List.of(
                new Book(1L, "BookTitle_1",
                        authorRepository.findById(1L).get(),
                        List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")),
                        commentRepository.findByBookId(1)),
                new Book(2L, "BookTitle_2",
                        authorRepository.findById(2L).get(),
                        List.of(new Genre(3L, "Genre_3"), new Genre(4L, "Genre_4")),
                        commentRepository.findByBookId(2)),
                new Book(3L, "BookTitle_3",
                        authorRepository.findById(3L).get(),
                        List.of(new Genre(5L, "Genre_5"), new Genre(6L, "Genre_6")),
                        commentRepository.findByBookId(3))
        );
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindById() {
        BookDto bookDto = bookService.findById(BOOK_ID);
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(bookMapper.bookToDto(expectedBooks.get(2)));
    }

    @Test
    void shouldNotThrowLazyExceptionWhenAccessingLazyFieldsFindAll() {
        List<BookDto> bookDtos = bookService.findAll();
        List<BookDto> expectedBookDtos = expectedBooks.stream().map(bookMapper::bookToDto).toList();
        assertThat(bookDtos).usingRecursiveComparison().isEqualTo(expectedBookDtos);
    }

    @Test
    void shouldDeleteById() {
        BookDto bookDto = bookService.insert(new CreateBookDto("Book", 2L, Set.of(2L, 5L)));
        bookService.deleteById(bookDto.id());
        assertThrows(EntityNotFoundException.class, () -> bookService.findById(bookDto.id()));
    }

    @Nested
    class InsertTests {
        @Test
        void shouldInsert() {
            BookDto bookDto = bookService.insert(new CreateBookDto("Book", 2L, Set.of(2L, 5L)));
            BookDto expectedBookDto = bookService.findById(bookDto.id());
            assertThat(bookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);
            bookService.deleteById(bookDto.id());
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
            BookDto bookDto = bookService.insert(new CreateBookDto("Book", 2L, Set.of(2L, 5L)));
            String updatedTitle = "UpdatedBook";
            long updatedAuthorId = 3L;
            Set<Long> updatedGenreIds = Set.of(5L, 6L);
            BookDto actualBookDto = bookService.update(new UpdateBookDto(bookDto.id(), updatedTitle
                    , updatedAuthorId, updatedGenreIds));
            BookDto expectedBookDto = bookMapper.bookToDto(new Book(actualBookDto.id(),
                    updatedTitle,
                    authorRepository.findById(updatedAuthorId).get(),
                    List.of(new Genre(5L, "Genre_5"), new Genre(6L, "Genre_6")),
                    null));
            assertThat(actualBookDto)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBookDto);
            bookService.deleteById(actualBookDto.id());
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
