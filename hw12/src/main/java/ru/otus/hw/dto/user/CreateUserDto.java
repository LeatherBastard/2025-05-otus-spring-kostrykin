package ru.otus.hw.dto.user;

import jakarta.persistence.Column;

public record CreateUserDto(String username, String password,String repeatPassword) {

}
