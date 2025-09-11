package ru.otus.hw.converters;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.mongo.AuthorDocument;

@Component
public class AuthorConverter {
    public AuthorDocument authorToAuthorDocument(Author author) {
        AuthorDocument authorDocument = new AuthorDocument();
        authorDocument.setId(new ObjectId().toString());
        authorDocument.setFullName(author.getFullName());
        return authorDocument;
    }
}
