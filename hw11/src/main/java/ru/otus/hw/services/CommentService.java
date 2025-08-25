package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CreateCommentDto;
import ru.otus.hw.dto.comment.UpdateCommentDto;

public interface CommentService {
    Mono<CommentDto> findById(String id);

    Flux<CommentDto> findAllByBookId(String id);

    Mono<CommentDto> insert(String bookId, CreateCommentDto commentDto);

    Mono<CommentDto> update(UpdateCommentDto commentDto);

    Mono<Void> deleteById(String id);


}
