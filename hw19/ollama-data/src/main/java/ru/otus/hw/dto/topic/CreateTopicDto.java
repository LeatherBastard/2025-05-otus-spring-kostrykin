package ru.otus.hw.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTopicDto(
        @NotNull
        @NotBlank(message = "User id should not be blank")
        String userId,
        @NotNull
        @NotBlank(message = "Title should not be blank")
        @Size(min = 4, message = "Topic title should be 4 symbols minimum")
        String title) {
}
