package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-entity-author-genres-graph");

        return Optional.ofNullable(
                entityManager.find(Book.class,
                        id,
                        Map.of("jakarta.persistence.fetchgraph", entityGraph)));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-entity-author-graph");
        TypedQuery<Book> query = entityManager
                .createQuery("select b from Book b", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }

}

