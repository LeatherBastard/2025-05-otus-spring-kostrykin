package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Genre;

@RepositoryRestResource(excerptProjection = GenreDto.class,collectionResourceRel = "genres", path = "genres")
public interface GenreRepository extends CrudRepository<Genre, Long> {
}
