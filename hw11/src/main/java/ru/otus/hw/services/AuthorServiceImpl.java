package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorMapper authorMapper;

    private final AuthorRepository authorRepository;


    @Override
    public Flux<AuthorDto> findAll() {
       return authorRepository.findAll().map(authorMapper::authorToDto);
    }
}
