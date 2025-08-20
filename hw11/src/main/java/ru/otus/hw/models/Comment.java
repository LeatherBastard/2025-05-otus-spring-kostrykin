package ru.otus.hw.models;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "comments")
public class Comment {
    @Id
    private final Long id;

    @NotNull
    private final Book book;

    @NotNull
    private String text;

    public void setText(@NotNull String text) {
        this.text = text;
    }

    @PersistenceCreator
    public Comment(Long id, @NotNull Book book, @NotNull String text) {
        this.id = id;
        this.book = book;
        this.text = text;
    }


}
