package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.author.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value = AuthorController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(AuthorMapper.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private AuthorMapper authorMapper;


    private List<Author> authors = List.of(new Author(1L, "Author_1"),
            new Author(2L, "Author_2"));

    @Test
    void shouldRenderAuthorsPage() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        mvc.perform(get("/authors"))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", authors.stream().map(authorMapper::authorToDto).toList()));
    }
}
