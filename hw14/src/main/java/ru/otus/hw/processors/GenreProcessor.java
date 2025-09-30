package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.GenreDocument;

@Component
@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, GenreDocument> {

    private final GenreConverter genreConverter;

    @Override
    public GenreDocument process(Genre genre) throws Exception {
        return genreConverter.genreToGenreDocument(genre);
    }
}
