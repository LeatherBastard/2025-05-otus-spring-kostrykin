package ru.otus.hw.dto.ollama;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Message;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String model;

    private List<Message> messages;

    private Boolean stream;
}
