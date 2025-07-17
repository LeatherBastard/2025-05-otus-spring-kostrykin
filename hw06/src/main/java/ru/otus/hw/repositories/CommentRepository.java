package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository {
    Comment findById(long id);

    List<Comment> findAllByBookId(long id);

    Comment save(Comment comment);

    void deleteById(long id);
}
