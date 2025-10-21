package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.clients.CommentClient;
import ru.otus.hw.dto.comment.CommentDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentClient commentClient;

    @Override
    public List<CommentDto> findAllByBookId(long id) {
        return commentClient.getCommentsByBookId(id);
    }


}
