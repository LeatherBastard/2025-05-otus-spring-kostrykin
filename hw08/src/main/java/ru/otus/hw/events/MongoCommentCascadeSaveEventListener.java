package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoCommentCascadeSaveEventListener extends AbstractMongoEventListener<Comment> {
    private final BookRepository bookRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Comment> event) {
        super.onBeforeConvert(event);
        var comment = event.getSource();
        if (comment.getBook() != null) {
            comment.getBook().getComments().add(comment);
            bookRepository.save(comment.getBook());
        }
    }
}
