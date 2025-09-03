package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.WebSecurityConfiguration;
import ru.otus.hw.controllers.GenreController;
import ru.otus.hw.services.genre.GenreService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import(WebSecurityConfiguration.class)
public class GenreControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Test
    public void testGetGenresOnUser() throws Exception {
        mvc.perform(get("/genres").with(user("user"))).andExpect(status().isOk());
    }

    @Test
    public void testGetGenresOnNotRegistered() throws Exception {
        mvc.perform(get("/genres")).andExpect(status().is3xxRedirection());
    }
}