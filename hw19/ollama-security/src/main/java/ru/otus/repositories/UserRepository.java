package ru.otus.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
