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
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Mono<CommentDto> findById(Long id) {
        return commentRepository.findById(id).map(commentMapper::commentToDto);
    }

    @Transactional
    @Override
    public Flux<CommentDto> findAllByBookId(Long id) {
        return commentRepository.findByBookId(id).map(commentMapper::commentToDto);
    }

    @Transactional
    @Override
    public Mono<CommentDto> insert(Long bookId, CreateCommentDto bookDto) {
        var bookMono = bookRepository.findById(bookId)
                .onErrorResume(e -> Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(bookId))));

        AtomicReference<Book> book = new AtomicReference<>();
        bookMono.subscribe(result -> {
            book.set(result);
        });
        var comment = new Comment(0L, book.get(), bookDto.text());

        return commentRepository.save(comment).map(commentMapper::commentToDto);
    }

    @Transactional
    @Override
    public Mono<CommentDto> update(UpdateCommentDto commentDto) {
        var updateCommentMono = commentRepository.findById(commentDto.id())
                .onErrorResume(e -> Mono.error(new EntityNotFoundException("Comment with id %s not found".formatted(commentDto.id()))));
        AtomicReference<Comment> updateComment = new AtomicReference<>();
        updateCommentMono.subscribe(result -> {
            updateComment.set(result);
        });
        updateComment.get().setText(commentDto.text());
        return commentRepository.save(updateComment.get()).map(commentMapper::commentToDto);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
