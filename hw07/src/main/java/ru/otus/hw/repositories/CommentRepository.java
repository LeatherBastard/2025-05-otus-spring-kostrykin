package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment,Long> {
    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long id);

    void deleteById(long id);
}
