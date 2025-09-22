package ru.otus.hw.converters;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.utils.cache.IdCache;

@Component
@RequiredArgsConstructor
public class GenreConverter {

    private final IdCache idCache;

    public GenreDocument genreToGenreDocument(Genre genre) {
        GenreDocument genreDocument = new GenreDocument();
        genreDocument.setId(idCache.putId(genre.getId() + Genre.class.getName()));
        genreDocument.setName(genre.getName());
        return genreDocument;
    }
}
