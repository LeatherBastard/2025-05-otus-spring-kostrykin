package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookMapper {

    private final GenreMapper genreMapper;

    public BookDto bookToDto(Book book) {

        return new BookDto(book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenres().stream().map(genreMapper::genreToDto).toList());

    }
}
