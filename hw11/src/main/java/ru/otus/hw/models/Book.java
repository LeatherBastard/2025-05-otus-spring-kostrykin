package ru.otus.hw.models;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table(name = "books")
public class Book {
    @Id
    private Long id;

    @NotNull
    private String title;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private Author author;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private List<Genre> genres;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private List<Comment> comments;

    @PersistenceCreator
    public Book(Long id,
                @NotNull String title,
                @NotNull Author author,
                @NotNull List<Genre> genres,
                @NotNull List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.comments = comments;
    }
}
