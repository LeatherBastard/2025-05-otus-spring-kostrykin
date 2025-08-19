package ru.otus.hw.models;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "authors")
public class Author {
    @Id
    private final Long id;

    @NotNull
    private final String fullName;

    @PersistenceCreator
    public Author(Long id, @NotNull String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
}
