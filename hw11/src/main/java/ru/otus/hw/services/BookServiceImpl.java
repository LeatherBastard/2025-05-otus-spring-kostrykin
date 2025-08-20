package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Mono<BookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::bookToDto)
                .onErrorResume(e -> Mono.error(new EntityNotFoundException(String.format("Book with id %d was not found", id))));
    }

    @Transactional
    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll().map(bookMapper::bookToDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> insert(CreateBookDto bookDto) {
        if (isEmpty(bookDto.genreIds())) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var authorMono = authorRepository.findById(bookDto.authorId())
                .onErrorResume(e ->
                        Mono.error(new EntityNotFoundException("Author with id %s not found".formatted(bookDto.authorId()))));
        AtomicReference<Author> author = new AtomicReference<>();
        authorMono.subscribe(result -> author.set(result));

        List<Genre> genres = genreRepository.findAllById(bookDto.genreIds()).toStream().toList();

        if (isEmpty(genres) || bookDto.genreIds().size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(bookDto.genreIds()));
        }

        var book = new Book(0L, bookDto.title(), author.get(), genres, new ArrayList<>());
        return bookRepository.save(book).map(bookMapper::bookToDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> update(UpdateBookDto bookDto) {

        var updateBookMono = bookRepository.findById(bookDto.id()).onErrorResume(e -> Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(bookDto.id()))));
        AtomicReference<Book> updateBook = new AtomicReference<>();
        updateBookMono.subscribe(result -> updateBook.set(result));

        var authorMono = authorRepository.findById(bookDto.authorId()).onErrorResume(e -> Mono.error(new EntityNotFoundException("Author with id %s not found"
                .formatted(bookDto.authorId()))));
        AtomicReference<Author> author = new AtomicReference<>();
        authorMono.subscribe(result -> author.set(result));


        List<Genre> genres = genreRepository.findAllById(bookDto.genreIds()).toStream().toList();

        if (isEmpty(genres) || bookDto.genreIds().size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(bookDto.genreIds()));
        }

        updateBook.get().setTitle(bookDto.title());
        updateBook.get().setAuthor(author.get());
        updateBook.get().setGenres(genres);

        return bookRepository.save(updateBook.get()).map(bookMapper::bookToDto);
    }


    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }


}
