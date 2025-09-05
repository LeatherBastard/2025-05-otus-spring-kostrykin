package ru.otus.hw.services.comment;

import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CreateCommentDto;
import ru.otus.hw.dto.comment.UpdateCommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {
    Comment findById(long id);

    List<Comment> findAllByBookId(long id);

    Comment insert(long bookId, CreateCommentDto commentDto);

    Comment update(UpdateCommentDto commentDto);

    void deleteById(long id);
}
