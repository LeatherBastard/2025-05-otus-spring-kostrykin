package ru.otus.services.user;


import ru.otus.dto.CreateUserDto;
import ru.otus.dto.UserDto;

public interface UserService {
    UserDto insert(CreateUserDto userDto);

}
