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
        var comment = commentRepository.findById(id).map(commentConverter::commentToDto).orElse(null);
        return Optional.ofNullable(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long id) {
        return commentRepository.findByBookId(id).stream().map(commentConverter::commentToDto).toList();
    }

    @Transactional
    @Override
    public CommentDto insert(long bookId, String text) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(0, book, text);
        return commentConverter.commentToDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(long id, String text) {
        var updateComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        updateComment.setText(text);
        return commentConverter.commentToDto(commentRepository.save(updateComment));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
