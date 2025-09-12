package ru.otus.hw.services.book;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.acl.AclServiceWrapperService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {


    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclServiceWrapperService;

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(#id,'ru.otus.hw.models.Book','READ')")
    @Override
    public Optional<Book> findById(long id) {
        var book = bookRepository.findById(id).orElse(null);
        return Optional.ofNullable(book);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'READ')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(CreateBookDto bookDto) {
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

        book = bookRepository.save(book);
        aclServiceWrapperService.createPermission(book);
        return book;
    }

    @Transactional
    @Override
    @PostAuthorize("hasPermission(#bookDto.id,'ru.otus.hw.models.Book', 'ADMINISTRATION')")
    public Book update(UpdateBookDto bookDto) {
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

        return bookRepository.save(updateBook);
    }

    @Transactional
    @Override
    @PreAuthorize("hasPermission(#id,'ru.otus.hw.models.Book','ADMINISTRATION')")
    public void deleteById(long id) {
        aclServiceWrapperService.deletePermissions(findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id))));
        bookRepository.deleteById(id);
    }


}
