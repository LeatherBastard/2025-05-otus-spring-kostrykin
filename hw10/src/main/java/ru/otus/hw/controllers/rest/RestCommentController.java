package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestCommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public List<CommentDto> getCommentsByBookId(@RequestParam long bookId) {
        return commentService.findAllByBookId(bookId);
    }
}
