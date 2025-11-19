package ru.otus.converters;

import org.springframework.stereotype.Component;
import ru.otus.dto.UserDto;
import ru.otus.models.User;


@Component
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}
