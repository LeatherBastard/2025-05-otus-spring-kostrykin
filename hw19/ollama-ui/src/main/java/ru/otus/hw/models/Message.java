package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String id;

    private String topicId;

    private String role;

    private String content;

    public Message(String topicId, String role, String content) {
        this.topicId = topicId;
        this.role = role;
        this.content = content;
    }
}
