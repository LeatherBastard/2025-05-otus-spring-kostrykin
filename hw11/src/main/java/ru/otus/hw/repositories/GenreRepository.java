package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

@Repository
public interface GenreRepository extends ReactiveCrudRepository<Genre, Long> {
}
