package ru.otus.hw.converters;


import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.GenreDocument;

@Component
public class GenreConverter {
    public GenreDocument genreToGenreDocument(Genre genre) {
        GenreDocument genreDocument = new GenreDocument();
        genreDocument.setId(new ObjectId().toString());
        genreDocument.setName(genre.getName());
        return genreDocument;
    }
}
