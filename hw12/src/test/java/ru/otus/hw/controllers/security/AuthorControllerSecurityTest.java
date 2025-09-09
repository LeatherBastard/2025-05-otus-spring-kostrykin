package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.WebSecurityConfiguration;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.services.AuthorService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(WebSecurityConfiguration.class)
public class AuthorControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @Test
    public void testGetAuthorsOnUser() throws Exception {
        mvc.perform(get("/authors").with(user("user"))).andExpect(status().isOk());
    }

    @Test
    public void testGetAuthorsOnNotRegistered() throws Exception {
        mvc.perform(get("/authors")).andExpect(status().is3xxRedirection());
    }
}
