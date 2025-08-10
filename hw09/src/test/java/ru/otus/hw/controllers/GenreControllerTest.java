package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;


    private List<GenreDto> genres = List.of(new GenreDto(1, "Genre_1"),
            new GenreDto(2, "Genre_2"));

    @Test
    void shouldRenderGenresPage() throws Exception {
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/genres"))
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", genres));
    }
}
