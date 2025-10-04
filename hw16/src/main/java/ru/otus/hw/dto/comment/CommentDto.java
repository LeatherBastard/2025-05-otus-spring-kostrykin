package ru.otus.hw.dto.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Comment;

@Projection(
        name = "commentDto",
        types = {Comment.class}
)
public interface CommentDto {
    @Value("#{target.id}")
    Long getId();

    String getText();
}
