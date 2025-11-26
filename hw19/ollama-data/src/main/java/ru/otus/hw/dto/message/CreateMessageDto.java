package ru.otus.hw.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMessageDto(@NotNull
                               @NotBlank(message = "Topic id should not be blank")
                               String topicId,
                               @NotNull
                               @NotBlank(message = "Role should not be blank")
                               String role,
                               @NotNull
                               @NotBlank(message = "Content should not be blank")
                               String content) {
}
