package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.mongo.CommentDocument;

@Component
@RequiredArgsConstructor
public class CommentConverter {
    private final BookConverter bookConverter;

    public CommentDocument commentToCommentDocument(Comment comment) {
        CommentDocument commentDocument = new CommentDocument();
        commentDocument.setId(new ObjectId().toString());
        commentDocument.setText(comment.getText());
        commentDocument.setBook(bookConverter.bookToBookDocument(comment.getBook()));
        return commentDocument;
    }

}
