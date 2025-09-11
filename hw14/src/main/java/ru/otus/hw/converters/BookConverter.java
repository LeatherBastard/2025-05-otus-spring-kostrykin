package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.mongo.BookDocument;

@Component
@RequiredArgsConstructor
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDocument bookToBookDocument(Book book) {
        BookDocument bookDocument = new BookDocument();
        bookDocument.setId(new ObjectId().toString());
        bookDocument.setTitle(book.getTitle());
        bookDocument.setAuthor(authorConverter.authorToAuthorDocument(book.getAuthor()));
        bookDocument.setGenres(book.getGenres().stream().map(genreConverter::genreToGenreDocument).toList());
        return bookDocument;
    }
}
