package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;

@Component
@RequiredArgsConstructor
public class CommentConverter {
    private final BookConverter bookConverter;

    private final MongoTemplate mongoTemplate;


    public CommentDocument commentToCommentDocument(Comment comment) {
        CommentDocument commentDocument = new CommentDocument();
        commentDocument.setId(new ObjectId().toString());
        commentDocument.setText(comment.getText());
        commentDocument.setBook(findBookByTitle(comment.getBook().getTitle()));
        return commentDocument;
    }


    public BookDocument findBookByTitle(String title) {
        Query query = new Query(Criteria.where("title").is(title));
        return mongoTemplate.findOne(query, BookDocument.class);
    }


}
