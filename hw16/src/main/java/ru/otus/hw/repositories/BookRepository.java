package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(excerptProjection = BookDto.class, collectionResourceRel = "books", path = "books")
public interface BookRepository extends CrudRepository<Book, Long> {
    @Override
    @EntityGraph(value = "book-entity-author-genres-graph")
    Optional<Book> findById(Long id);

    @EntityGraph(value = "book-entity-author-graph")
    List<Book> findAll();

}
