package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;


public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByBookId(String bookId);

    Mono<Void> deleteAllByBookId(String bookId);

    Mono<Boolean> existsById(String id);
}
