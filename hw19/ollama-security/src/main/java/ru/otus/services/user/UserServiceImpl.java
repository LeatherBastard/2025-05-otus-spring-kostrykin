package ru.otus.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.converters.UserMapper;
import ru.otus.dto.CreateUserDto;
import ru.otus.dto.UserDto;
import ru.otus.exceptions.EntityAlreadyExistsException;
import ru.otus.models.User;
import ru.otus.repositories.UserRepository;

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
