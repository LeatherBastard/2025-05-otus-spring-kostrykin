package ru.otus.hw.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateBookDto(long id,
                            @NotBlank(message = "Title should not be blank")
                            @Size(min = 4, message = "Title should be 4 symbols minimum")
                            String title, long authorId, Set<Long> genreIds) {
}
