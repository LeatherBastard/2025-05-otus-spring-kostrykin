package ru.otus.hw.dto.book;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

@Projection(
        name = "bookDto",
        types = {Book.class}
)
public interface BookDto {
    @Value("#{target.id}")
    Long getId();

    String getTitle();

    Author getAuthor();

    List<Genre> getGenres();
}
