package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;

@RepositoryRestResource(excerptProjection = CommentDto.class, collectionResourceRel = "comments", path = "comments")
public interface CommentRepository extends CrudRepository<Comment, Long> {
    @RestResource(path = "byBook")
    List<Comment> findByBookId(@Param("bookId") long id);
}
