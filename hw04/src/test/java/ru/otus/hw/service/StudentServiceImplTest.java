package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;

@SpringBootTest
class StudentServiceImplTest {

    @MockitoBean
    private LocalizedIOService ioService;

    @Autowired
    private StudentService studentService;

    @Test
    void whenDetermineCurrentStudent_thenSuccessful() {
        studentService.determineCurrentStudent();
        verify(ioService, times(2)).readStringWithPromptLocalized(anyString());

    }
}
