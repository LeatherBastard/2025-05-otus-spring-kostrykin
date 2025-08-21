package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CreateCommentDto;
import ru.otus.hw.dto.comment.UpdateCommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Mono<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .map(commentMapper::commentToDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id %s not found".formatted(id))));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
                .map(commentMapper::commentToDto);
    }

    @Transactional
    @Override
    public Mono<CommentDto> insert(String bookId, CreateCommentDto commentDto) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(bookId))))
                .flatMap(book -> {
                    Comment comment = new Comment(book.getId(), commentDto.text());
                    return commentRepository.save(comment)
                            .map(commentMapper::commentToDto);
                });
    }

    @Transactional
    @Override
    public Mono<CommentDto> update(UpdateCommentDto commentDto) {
        return commentRepository.findById(commentDto.id())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id %s not found".formatted(commentDto.id()))))
                .flatMap(updateComment -> {
                    updateComment.setText(commentDto.text());
                    return commentRepository.save(updateComment)
                            .map(commentMapper::commentToDto);
                });
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id %s not found".formatted(id))))
                .flatMap(comment -> commentRepository.deleteById(id));
    }


    @Transactional(readOnly = true)
    public Flux<CommentDto> findAll() {
        return commentRepository.findAll()
                .map(commentMapper::commentToDto);
    }

}

