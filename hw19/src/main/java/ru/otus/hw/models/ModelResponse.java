package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelResponse {
    private String model;
    private LocalDateTime createdAt;
    private Message message;
    private boolean done;
}
