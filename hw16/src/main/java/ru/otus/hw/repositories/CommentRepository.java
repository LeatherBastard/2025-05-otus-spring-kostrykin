package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Comment;

import java.util.List;

@RepositoryRestResource(excerptProjection = CommentDto.class,collectionResourceRel = "comments", path = "comments")
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByBookId(long id);
}
