package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.services.author.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @GetMapping("/authors")
    public String allAuthorsPage(Model model) {
        List<AuthorDto> authorDtos = authorService.findAll().stream().map(authorMapper::authorToDto).toList();
        model.addAttribute("authors", authorDtos);
        return "authors";
    }
}
