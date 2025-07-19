package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentConverter commentConverter;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(long id) {
        var comment = commentRepository.findById(id);
        CommentDto commentDto = null;
        if (comment.isPresent()) {
            commentDto = commentConverter.commentToDto(comment.get());
        }
        return Optional.ofNullable(commentDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long id) {
        return commentRepository.findAllByBookId(id).stream().map(commentConverter::commentToDto).toList();
    }

    @Transactional
    @Override
    public CommentDto insert(long bookId, String text) {
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %s not found".formatted(bookId));
        }
        var comment = new Comment(0, book.get(), text);
        return commentConverter.commentToDto(comment);
    }

    @Transactional
    @Override
    public CommentDto update(long id, String text) {

        var updateComment = commentRepository.findById(id);
        if (updateComment.isEmpty()) {
            throw new EntityNotFoundException("Comment with id %s not found".formatted(id));
        }
        Comment comment = updateComment.get();
        comment.setText(text);
        return commentConverter.commentToDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
