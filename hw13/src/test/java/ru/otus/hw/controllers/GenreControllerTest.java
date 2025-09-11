package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.genre.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value = GenreController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(GenreMapper.class)
public class GenreControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private GenreMapper genreMapper;


    private List<Genre> genres = List.of(new Genre(1L, "Genre_1"),
            new Genre(2L, "Genre_2"));

    @Test
    void shouldRenderGenresPage() throws Exception {
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/genres"))
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", genres.stream().map(genreMapper::genreToDto).toList()));
    }
}
