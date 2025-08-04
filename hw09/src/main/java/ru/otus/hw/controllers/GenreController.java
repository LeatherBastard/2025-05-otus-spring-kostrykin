package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public String allGenresPage(Model model) {
        List<GenreDto> genreDtos = genreService.findAll();
        model.addAttribute("genres", genreDtos);
        return "genres";
    }
}
