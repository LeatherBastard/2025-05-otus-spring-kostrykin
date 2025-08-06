package ru.otus.hw.services.plain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.AuthorServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({AuthorServiceImpl.class, AuthorConverter.class})
class AuthorServiceIntegrationTest {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private AuthorConverter authorConverter;


    @AfterEach
    void dropDb() {
        repository.deleteAll();
    }


    @Test
    void shouldFindAllAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(repository.save(new Author("Author_1")));
        authors.add(repository.save(new Author("Author_2")));
        authors.add(repository.save(new Author("Author_3")));
        List<AuthorDto> authorDtos = authorService.findAll();
        assertThat(authorDtos).usingRecursiveComparison()
                .isEqualTo(authors.stream().map(authorConverter::authorToDto).toList());

    }

}
