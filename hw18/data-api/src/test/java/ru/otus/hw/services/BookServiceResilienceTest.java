package ru.otus.hw.services;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BookServiceResilienceTest {
    @Autowired
    BookService bookService;


    @Test
    void shouldThrowRequestNotPermittedWhenFindAll() {
        for (int i = 0; i < 8; i++) {
            bookService.findAll();
        }
        assertThatThrownBy(() -> bookService.findAll())
                .isExactlyInstanceOf(RequestNotPermitted.class);
    }

}
