package ru.otus.hw.services.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Comment findById(long id) {
        var comment = commentRepository.findById(id).orElse(null);
        return comment;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllByBookId(long id) {
        return commentRepository.findByBookId(id);
    }

    @Transactional
    @Override
    public Comment insert(long bookId, CreateCommentDto bookDto) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(0, book, bookDto.text());
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(UpdateCommentDto bookDto) {
        var updateComment = commentRepository.findById(bookDto.id())
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(bookDto.id())));
        updateComment.setText(bookDto.text());
        return commentRepository.save(updateComment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
