package ru.otus.hw.models.mongo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentDocument {
    @Id
    private String id;

    @DBRef(lazy = true)
    private BookDocument book;

    @Field(name = "text")
    private String text;

    public CommentDocument(BookDocument book, String text) {
        this.book = book;
        this.text = text;
    }


}
