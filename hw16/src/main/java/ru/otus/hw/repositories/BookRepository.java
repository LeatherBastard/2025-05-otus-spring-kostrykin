package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Override
    @EntityGraph(value = "book-entity-author-genres-graph")
    Optional<Book> findById(Long id);

    @EntityGraph(value = "book-entity-author-graph")
    List<Book> findAll();

}
