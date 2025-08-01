package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
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
    public Optional<BookDto> findById(long id) {

        var book = bookRepository.findById(id);
        BookDto bookDto = null;
        if (book.isPresent()) {
            bookDto = bookConverter.bookToDto(book.get());
        }
        return Optional.ofNullable(bookDto);

    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookConverter::bookToDto).toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(authorId);
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id %s not found".formatted(authorId));
        }
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(0, title, author.get(), genres, new ArrayList<>());
        return bookConverter.bookToDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        var updateBook = bookRepository.findById(id);
        if (updateBook.isEmpty()) {
            throw new EntityNotFoundException("Book with id %s not found".formatted(id));
        }
        var author = authorRepository.findById(authorId);

        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id %s not found".formatted(authorId));
        }
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        Book book = updateBook.get();
        book.setTitle(title);
        book.setAuthor(author.get());
        book.setGenres(genres);

        return bookConverter.bookToDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }


}
