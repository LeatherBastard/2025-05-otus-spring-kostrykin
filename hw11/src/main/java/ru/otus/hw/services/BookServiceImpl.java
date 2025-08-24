package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
@Log4j2
public class BookServiceImpl implements BookService {


    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .map(bookMapper::bookToDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id))));
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(bookMapper::bookToDto);
    }

    @Override
    public Mono<BookDto> insert(CreateBookDto bookDto) {
        return authorRepository.findById(bookDto.authorId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %s not found"
                        .formatted(bookDto.authorId()))))
                .flatMap(author -> genreRepository.findAllById(bookDto.genreIds())
                        .collectList()
                        .flatMap(genres -> {
                            if (genres.isEmpty() || bookDto.genreIds().size() != genres.size()) {
                                return Mono.error(new EntityNotFoundException(
                                        "One or all genres with ids %s not found".formatted(bookDto.genreIds()))
                                );
                            }
                            Book book = new Book(bookDto.title(), author, genres);
                            return bookRepository.save(book)
                                    .map(bookMapper::bookToDto);
                        })
                );
    }

    @Override
    public Mono<BookDto> update(UpdateBookDto bookDto) {
        return bookRepository.findById(bookDto.id())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found"
                        .formatted(bookDto.id()))))
                .flatMap(updateBook -> authorRepository.findById(bookDto.authorId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %s not found"
                                .formatted(bookDto.authorId()))))
                        .flatMap(author -> genreRepository.findAllById(bookDto.genreIds())
                                .collectList()
                                .flatMap(genres -> {
                                    if (genres.isEmpty() || bookDto.genreIds().size() != genres.size()) {
                                        return Mono.error(new EntityNotFoundException(
                                                "One or all genres with ids %s not found".formatted(bookDto.genreIds()))
                                        );
                                    }
                                    updateBook.setTitle(bookDto.title());
                                    updateBook.setAuthor(author);
                                    updateBook.setGenres(genres);
                                    return bookRepository.save(updateBook)
                                            .map(bookMapper::bookToDto);
                                })
                        )
                );
    }

    @Override
    public Mono<Void> deleteById(String id) {
                    return bookRepository.deleteById(id);
    }


}
