package ru.otus.hw.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.UserMapper;
import ru.otus.hw.dto.user.CreateUserDto;
import ru.otus.hw.dto.user.UserDto;
import ru.otus.hw.exceptions.EntityAlreadyExistsException;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDto insert(CreateUserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("User with username %s already exists",
                    userDto.username()));
        }
        var user = new User(0, userDto.username(), passwordEncoder.encode(userDto.password()), "USER");
        return userMapper.userToDto(userRepository.save(user));
    }
}
