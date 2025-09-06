package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.user.CreateUserDto;
import ru.otus.hw.exceptions.EntityAlreadyExistsException;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value = MainController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @Test
    void shouldRenderMainPage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    void shouldRenderLoginPage() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }

    @Test
    void shouldRenderRegisterPage() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(view().name("register"));
    }

    @Test
    void shouldRenderErrorPageWhenUserAlreadyExist() throws Exception {
        when(userService.insert(any())).thenThrow(new EntityAlreadyExistsException("Just exception"));
        mvc.perform(post("/register").flashAttr("userDto", new CreateUserDto("", "", "")))
                .andExpect(view().name("customError"))
                .andExpect(status().isBadRequest());
    }
}
