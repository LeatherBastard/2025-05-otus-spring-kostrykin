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
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    @Field(name = "topicId")
    private String topicId;

    @Field(name = "role")
    private String role;

    @Field(name = "content")
    private String content;

    public Message(String topicId, String role, String content) {
        this.topicId = topicId;
        this.role = role;
        this.content = content;
    }
}
