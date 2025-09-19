package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.utils.cache.IdCache;

@Component
@RequiredArgsConstructor
public class AuthorConverter {

    private final IdCache idCache;

    public AuthorDocument authorToAuthorDocument(Author author) {
        AuthorDocument authorDocument = new AuthorDocument();
        authorDocument.setId(idCache.putId(author.getId()+Author.class.getName()));
        authorDocument.setFullName(author.getFullName());
        return authorDocument;
    }
}
