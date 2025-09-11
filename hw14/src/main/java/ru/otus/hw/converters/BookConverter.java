package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.GenreDocument;

@Component
@RequiredArgsConstructor
public class BookConverter {


    private final MongoTemplate mongoTemplate;

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDocument bookToBookDocument(Book book) {
        BookDocument bookDocument = new BookDocument();
        bookDocument.setId(new ObjectId().toString());
        bookDocument.setTitle(book.getTitle());
        bookDocument.setAuthor(findAuthorDocumentByName(book.getAuthor().getFullName()));
        bookDocument.setGenres(book.getGenres().stream().map(Genre::getName)
                .map(this::findGenreDocumentByName).toList());
        return bookDocument;

    }

    private GenreDocument findGenreDocumentByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, GenreDocument.class);
    }

    public AuthorDocument findAuthorDocumentByName(String name) {
        Query query = new Query(Criteria.where("full_name").is(name));
        return mongoTemplate.findOne(query, AuthorDocument.class);
    }

}
