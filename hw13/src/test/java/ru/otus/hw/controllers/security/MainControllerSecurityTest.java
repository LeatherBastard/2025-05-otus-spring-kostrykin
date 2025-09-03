package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.WebSecurityConfiguration;
import ru.otus.hw.controllers.MainController;
import ru.otus.hw.dto.user.CreateUserDto;
import ru.otus.hw.services.user.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MainController.class)
@Import(WebSecurityConfiguration.class)
public class MainControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void testIndexOnUser() throws Exception {
        mvc.perform(get("/").with(user("user"))).andExpect(status().isOk());
    }

    @Test
    public void testIndexOnNotRegistered() throws Exception {
        mvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testLoginOnUser() throws Exception {
        mvc.perform(get("/login").with(user("user"))).andExpect(status().isOk());
    }

    @Test
    public void testLoginOnNotRegistered() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    public void testGetRegisterOnUser() throws Exception {
        mvc.perform(get("/register").with(user("user"))).andExpect(status().isOk());
    }

    @Test
    public void testGetRegisterOnNotRegistered() throws Exception {
        mvc.perform(get("/register")).andExpect(status().isOk());
    }

    @Test
    public void testPostRegisterOnUser() throws Exception {
        mvc.perform(post("/register")
                        .flashAttr("userDto", new CreateUserDto("", "", ""))
                        .with(user("user")))
                .andExpect(view().name("redirect:/"));
        verify(userService, times(1)).insert(any());

    }

    @Test
    public void testPostRegisterOnNotRegistered() throws Exception {
        mvc.perform(post("/register")
                        .flashAttr("userDto", new CreateUserDto("", "", "")))
                .andExpect(view().name("redirect:/"));
        verify(userService, times(1)).insert(any());
    }


}
