package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.user.UserDto;
import ru.otus.hw.models.User;

@Component
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}
