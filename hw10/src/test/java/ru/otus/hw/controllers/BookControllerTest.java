package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.view.BookController;
import ru.otus.hw.services.BookService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Test
    void shouldRenderBooksPage() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(view().name("books"));
    }

    @Test
    void shouldRenderAddPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(view().name("addbook"));
    }

    @Test
    void shouldRenderBookByIdPageWithCorrectViewAndModelAttributes() throws Exception {
        mvc.perform(get("/books/").param("bookId", "1"))
                .andExpect(view().name("book"));
    }


    @Test
    void shouldRenderErrorPageWhenWrongRequestParam() throws Exception {
        mvc.perform(get("/books/").param("bookId", "ab"))
                .andExpect(view().name("customError")).andExpect(status().isBadRequest());
    }

}
