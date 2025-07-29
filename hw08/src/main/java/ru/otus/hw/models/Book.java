package ru.otus.hw.models;

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
public class Book {

    @Id
    private long id;

    @Field(name = "title")
    private String title;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude

    private Author author;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Genre> genres;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Comment> comments;
}
