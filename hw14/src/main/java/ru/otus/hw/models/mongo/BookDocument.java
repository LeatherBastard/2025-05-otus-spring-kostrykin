package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class BookDocument {

    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AuthorDocument author;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<GenreDocument> genres;


    public BookDocument(String title, AuthorDocument author, List<GenreDocument> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}
