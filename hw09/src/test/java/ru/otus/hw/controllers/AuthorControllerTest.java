package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;


    private List<AuthorDto> authors = List.of(new AuthorDto(1, "Author_1"),
            new AuthorDto(2, "Author_2"));

    @Test
    void shouldRenderAuthorsPage() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        mvc.perform(get("/authors"))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", authors));
    }
}
