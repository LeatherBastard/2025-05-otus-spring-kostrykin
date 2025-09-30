package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.mongo.CommentDocument;

@Component
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, CommentDocument> {
    private final CommentConverter commentConverter;

    @Override
    public CommentDocument process(Comment comment) throws Exception {
        return commentConverter.commentToCommentDocument(comment);
    }

}

