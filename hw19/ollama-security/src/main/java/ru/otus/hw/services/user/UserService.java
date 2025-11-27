package ru.otus.hw.services.user;


import ru.otus.hw.dto.CreateUserDto;
import ru.otus.hw.dto.UserDto;

public interface UserService {
    UserDto insert(CreateUserDto userDto);

}
