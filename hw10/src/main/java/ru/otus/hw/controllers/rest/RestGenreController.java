package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestGenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public List<GenreDto> getGenres() {
        return genreService.findAll();
    }
}
