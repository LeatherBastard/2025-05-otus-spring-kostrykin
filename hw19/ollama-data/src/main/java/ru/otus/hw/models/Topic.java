package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "topics")
public class Topic {
    @Id
    private String id;
    @Field(name = "userId")
    private String userId;
    @Field(name = "name")
    private String name;

    public Topic(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
