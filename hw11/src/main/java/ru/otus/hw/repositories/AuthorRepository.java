package ru.otus.hw.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;

import java.lang.annotation.Native;

@Repository
public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {
    @Query("SELECT * FROM authors")
    Flux<Author> findAll();
}
