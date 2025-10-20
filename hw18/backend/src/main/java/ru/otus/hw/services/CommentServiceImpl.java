package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentMapper;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CreateCommentDto;
import ru.otus.hw.dto.comment.UpdateCommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(long id) {
        var comment = commentRepository.findById(id).map(commentMapper::commentToDto).orElse(null);
        return comment;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long id) {
        return commentRepository.findByBookId(id).stream().map(commentMapper::commentToDto).toList();
    }

    @Transactional
    @Override
    public CommentDto insert(long bookId, CreateCommentDto bookDto) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(0, book, bookDto.text());
        return commentMapper.commentToDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(UpdateCommentDto bookDto) {
        var updateComment = commentRepository.findById(bookDto.id())
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(bookDto.id())));
        updateComment.setText(bookDto.text());
        return commentMapper.commentToDto(commentRepository.save(updateComment));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
