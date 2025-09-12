package ru.otus.hw.services.user;

import ru.otus.hw.dto.user.CreateUserDto;
import ru.otus.hw.dto.user.UserDto;

public interface UserService {
    UserDto insert(CreateUserDto userDto);

}
