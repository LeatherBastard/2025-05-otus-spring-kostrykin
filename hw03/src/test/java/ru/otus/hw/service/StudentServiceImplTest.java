package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class StudentServiceImplTest {
    private final LocalizedIOService ioService = Mockito.mock(LocalizedIOService.class);
    private StudentService studentService;

    @BeforeEach
    void initialize() {
        studentService = new StudentServiceImpl(ioService);
    }

    @Test
    void whenDetermineCurrentStudent_thenSuccessful() {
        studentService.determineCurrentStudent();
        verify(ioService, times(2)).readStringWithPromptLocalized(anyString());

    }
}
