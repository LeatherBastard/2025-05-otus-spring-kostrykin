package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotNull
        @NotBlank(message = "Username should not be blank")
        @Size(min = 4, message = "Username should be 4 symbols minimum")
        String username,
        @Size(min = 4, message = "Password should be 4 symbols minimum")
        String password,
        String repeatPassword) {

}
