package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {
    Flux<Comment> findByBookId(long id);
}
