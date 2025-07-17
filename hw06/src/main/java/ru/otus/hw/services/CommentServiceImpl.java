package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
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
        Comment comment = commentRepository.findById(id);
        CommentDto commentDto = null;
        if (comment != null) {
            commentDto = commentConverter.commentToDto(comment);
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
        return commentConverter.commentToDto(save(0, bookId, text));
    }

    @Transactional
    @Override
    public CommentDto update(long id, long bookId, String text) {
        return commentConverter.commentToDto(save(id, bookId, text));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, long bookId, String text) {
        var book = bookRepository.findById(bookId);
        var comment = new Comment(id, book, text);
        return commentRepository.save(comment);
    }
}
