package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.utils.cache.IdCache;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    private final MongoTemplate mongoTemplate;

    private final IdCache idCache;

    public CommentDocument commentToCommentDocument(Comment comment) {
        CommentDocument commentDocument = new CommentDocument();
        commentDocument.setId(idCache.putId(comment.getId() + Comment.class.getName()));
        commentDocument.setText(comment.getText());
        commentDocument.setBook(findBookById(comment.getBook().getId() + Book.class.getName()));
        return commentDocument;
    }


    public BookDocument findBookById(String key) {
        return mongoTemplate.findById(idCache.getId(key), BookDocument.class);
    }

}
