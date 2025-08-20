package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CreateCommentDto;
import ru.otus.hw.dto.comment.UpdateCommentDto;

public interface CommentService {
    Mono<CommentDto> findById(Long id);

    Flux<CommentDto> findAllByBookId(Long id);

    Mono<CommentDto> insert(Long bookId, CreateCommentDto commentDto);

    Mono<CommentDto> update(UpdateCommentDto commentDto);

    void deleteById(Long id);
}
