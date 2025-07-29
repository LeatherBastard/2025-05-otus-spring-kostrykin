package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookConverter bookConverter;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(String id) {
        var book = bookRepository.findById(id).map(bookConverter::bookToDto).orElse(null);
        return Optional.ofNullable(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookConverter::bookToDto).toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));

        List<Genre> genres = new ArrayList<>();
        genreRepository.findAllById(genresIds).forEach(genre -> genres.add(genre));
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(0, title, author, genres, new ArrayList<>());
        return bookConverter.bookToDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(String id, String title, String authorId, Set<String> genresIds) {
        var updateBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));

        List<Genre> genres = new ArrayList<>();
        genreRepository.findAllById(genresIds).forEach(genre -> genres.add(genre));
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        updateBook.setTitle(title);
        updateBook.setAuthor(author);
        updateBook.setGenres(genres);

        return bookConverter.bookToDto(bookRepository.save(updateBook));
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }


}
