package ru.otus.hw.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({BookServiceImpl.class})
public class BookServiceIntegrationTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository repository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;


    @Autowired
    private BookConverter bookConverter;


    @AfterEach
    void dropDb() {
        repository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();
    }

    @Test
    void shouldFindAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(repository.save(
                new Book("BookTitle_1",
                        new Author("Author_1"),
                        List.of(new Genre("Genre_1"), new Genre("Genre_2")))));
        books.add(repository.save(
                new Book("BookTitle_2",
                        new Author("Author_2"),
                        List.of(new Genre("Genre_3"), new Genre("Genre_4")))));
        books.add(repository.save(
                new Book("BookTitle_3",
                        new Author("Author_3"),
                        List.of(new Genre("Genre_5"), new Genre("Genre_6")))));
        List<BookDto> bookDtos = bookService.findAll();
        assertThat(bookDtos).usingRecursiveComparison()
                .isEqualTo(books.stream().map(bookConverter::bookToDto).toList());
    }

    @Test
    void shouldFindBookById() {
        Book expectedBook = new Book("BookTitle_1",
                new Author("Author_1"),
                List.of(new Genre("Genre_1"), new Genre("Genre_2")));
        expectedBook = repository.save(expectedBook);
        BookDto actualBook = bookService.findById(expectedBook.getId()).get();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(bookConverter.bookToDto(expectedBook));
    }


    @Test
    void shouldInsertBook() {
        Author author = authorRepository.save(new Author("Author_1"));
        Genre genre1 = genreRepository.save(new Genre("Genre_1"));
        Genre genre2 = genreRepository.save(new Genre("Genre_2"));

        Book expectedBook = new Book("BookTitle_1",
                author,
                List.of(genre1, genre2));
        BookDto actualBook = bookService.insert(expectedBook.getTitle(), author.getId(), Set.of(genre1.getId(), genre2.getId()));
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(bookConverter.bookToDto(expectedBook));
    }


    @Test
    void shouldDeleteBookById() {
        Book expectedBook = new Book("BookTitle_1",
                new Author("Author_1"),
                List.of(new Genre("Genre_1"), new Genre("Genre_2")));
        expectedBook = repository.save(expectedBook);
        bookService.deleteById(expectedBook.getId());
        assertThat(repository.findById(expectedBook.getId()).isEmpty());
    }

    @Test
    void shouldUpdateBookById() {
        List<Genre> genres = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        authors.add(authorRepository.save(new Author("Author_1")));
        authors.add(authorRepository.save(new Author("Author_2")));
        genres.add(genreRepository.save(new Genre("Genre_1")));
        genres.add(genreRepository.save(new Genre("Genre_2")));
        genres.add(genreRepository.save(new Genre("Genre_3")));
        genres.add(genreRepository.save(new Genre("Genre_4")));

        BookDto bookDto = bookService.insert("Book", authors.get(0).getId(),
                Set.of(genres.get(0).getId(), genres.get(1).getId()));
        String updatedTitle = "UpdatedBook";
        String updatedAuthorId = authors.get(1).getId();
        Set<String> updatedGenreIds = Set.of(genres.get(2).getId(), genres.get(3).getId());
        BookDto actualBookDto = bookService.update(bookDto.id(), updatedTitle, updatedAuthorId, updatedGenreIds);
        BookDto expectedBookDto = bookConverter.bookToDto(
                new Book(actualBookDto.id(),
                        authors.get(1),
                        List.of(genres.get(2), genres.get(3))));
        assertThat(actualBookDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedBookDto);
    }
}
