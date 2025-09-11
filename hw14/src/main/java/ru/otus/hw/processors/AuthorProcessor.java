package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.mongo.AuthorDocument;

@Component
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, AuthorDocument> {

    private final AuthorConverter authorConverter;

    @Override
    public AuthorDocument process(Author author) throws Exception {
       return authorConverter.authorToAuthorDocument(author);
    }
}
