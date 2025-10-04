package ru.otus.hw.dto.author;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Author;

@Projection(
        name = "authorDto",
        types = {Author.class}
)
public interface AuthorDto {
    @Value("#{target.id}")
    Long getId();

    String getFullName();
}
