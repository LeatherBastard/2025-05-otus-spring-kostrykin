package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.models.Author;

@RepositoryRestResource(excerptProjection = AuthorDto.class,collectionResourceRel = "authors", path = "authors")
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
