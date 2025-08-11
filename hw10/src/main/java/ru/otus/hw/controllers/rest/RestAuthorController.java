package ru.otus.hw.controllers.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestAuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public List<AuthorDto> getAuthors() {
        return authorService.findAll();
    }
}
