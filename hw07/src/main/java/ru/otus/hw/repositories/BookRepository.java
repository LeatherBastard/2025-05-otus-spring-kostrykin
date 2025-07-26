package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    @EntityGraph(attributePaths = {"author", "genres"})
    Optional<Book> findById(long id);

    @EntityGraph(attributePaths = {"author"})
    List<Book> findAll();

    void deleteById(long id);
}
