package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<GenreDto> genres = List.of(new GenreDto(1, "Genre_1"),
            new GenreDto(2, "Genre_2"));


    @Test
    void shouldReturnGenres() throws Exception {
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/api/genres")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(genres)));
    }


}
