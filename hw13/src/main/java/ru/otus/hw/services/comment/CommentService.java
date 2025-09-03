package ru.otus.hw.services.comment;

import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CreateCommentDto;
import ru.otus.hw.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findAllByBookId(long id);

    CommentDto insert(long bookId, CreateCommentDto commentDto);

    CommentDto update(UpdateCommentDto commentDto);

    void deleteById(long id);
}
