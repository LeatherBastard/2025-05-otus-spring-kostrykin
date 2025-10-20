package ru.otus.hw.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.comment.CommentDto;

import java.util.List;

@FeignClient(name = "comment-client", url = "http://localhost:8080/api")
public interface CommentClient {
    @GetMapping("/comments")
    List<CommentDto> getCommentsByBookId(@RequestParam long bookId);
}
