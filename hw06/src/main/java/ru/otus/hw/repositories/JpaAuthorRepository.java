package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = entityManager
                .createQuery("select a from Author a", Author.class);
        return query.getResultList();

    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

}
