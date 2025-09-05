package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.genre.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping("/genres")
    public String allGenresPage(Model model) {
        List<GenreDto> genreDtos = genreService.findAll().stream().map(genreMapper::genreToDto).toList();
        model.addAttribute("genres", genreDtos);
        return "genres";
    }
}
