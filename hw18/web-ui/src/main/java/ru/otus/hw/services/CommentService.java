package ru.otus.hw.services;

import ru.otus.hw.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findAllByBookId(long id);
}
