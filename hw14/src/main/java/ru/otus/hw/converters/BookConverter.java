package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.utils.cache.IdCache;

@Component
@RequiredArgsConstructor
public class BookConverter {

    private final MongoTemplate mongoTemplate;

    private final IdCache idCache;

    public BookDocument bookToBookDocument(Book book) {
        BookDocument bookDocument = new BookDocument();
        bookDocument.setId(idCache.putId(book.getId() + Book.class.getName()));
        bookDocument.setTitle(book.getTitle());
        bookDocument.setAuthor(findAuthorDocumentById(book.getAuthor().getId() + Author.class.getName()));
        bookDocument.setGenres(book.getGenres().stream().map(Genre::getId)
                .map(id -> findGenreDocumentById(id + Genre.class.getName())).toList());
        return bookDocument;
    }

    private GenreDocument findGenreDocumentById(String key) {
        return mongoTemplate.findById(idCache.getId(key), GenreDocument.class);
    }

    public AuthorDocument findAuthorDocumentById(String key) {
        return mongoTemplate.findById(idCache.getId(key), AuthorDocument.class);
    }

}
