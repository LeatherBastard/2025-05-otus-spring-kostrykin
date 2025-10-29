package ru.otus.hw.services;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RateLimiter(name = "dataRateLimitedService")
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorMapper authorMapper;

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        List<AuthorDto> authorDtos = new ArrayList<>();
        authorRepository.findAll().forEach(
                author -> {
                    authorDtos.add(authorMapper.authorToDto(author));
                }
        );
        return authorDtos;
    }
}
