package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.clients.AuthorClient;
import ru.otus.hw.dto.author.AuthorDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorClient authorClient;


    @Override
    public List<AuthorDto> findAll() {
        return authorClient.getAuthors();
    }
}
