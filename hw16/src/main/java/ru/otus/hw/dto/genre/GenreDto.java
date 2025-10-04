package ru.otus.hw.dto.genre;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Genre;

@Projection(
        name = "genreDto",
        types = {Genre.class}
)
public interface GenreDto {
    @Value("#{target.id}")
    Long getId();

    String getName();
}
