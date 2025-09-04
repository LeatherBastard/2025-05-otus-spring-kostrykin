package ru.otus.hw.dto.book;




import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateBookDto(
        @NotNull
        @NotBlank(message = "Title should not be blank")
        @Size(min = 4, message = "Title should be 4 symbols minimum")
        String title,
        @NotNull
        Long authorId,
        @NotEmpty
        Set<Long> genreIds) {
}
