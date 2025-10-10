package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return bookRepository.findById(id).map(bookMapper::bookToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Book with id %d was not found", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::bookToDto).toList();
    }

    @Transactional
    @Override
    public BookDto insert(CreateBookDto bookDto) {
        if (isEmpty(bookDto.genreIds())) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(bookDto.authorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with id %s not found".formatted(bookDto.authorId())));

        List<Genre> genres = new ArrayList<>();
        genreRepository.findAllById(bookDto.genreIds()).forEach(genres::add);
        if (isEmpty(genres) || bookDto.genreIds().size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(bookDto.genreIds()));
        }

        var book = new Book(0, bookDto.title(), author, genres, new ArrayList<>());
        return bookMapper.bookToDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(UpdateBookDto bookDto) {
        var updateBook = bookRepository.findById(bookDto.id())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookDto.id())));

        var author = authorRepository.findById(bookDto.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found"
                        .formatted(bookDto.authorId())));

        List<Genre> genres = new ArrayList<>();
        genreRepository.findAllById(bookDto.genreIds()).forEach(genres::add);
        if (isEmpty(genres) || bookDto.genreIds().size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(bookDto.genreIds()));
        }

        updateBook.setTitle(bookDto.title());
        updateBook.setAuthor(author);
        updateBook.setGenres(genres);

        return bookMapper.bookToDto(bookRepository.save(updateBook));
    }


    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }


}
