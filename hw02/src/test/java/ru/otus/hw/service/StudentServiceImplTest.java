package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class StudentServiceImplTest {
    private final IOService ioService = Mockito.mock(IOService.class);
    private StudentService studentService;

    @BeforeEach
    void initialize() {
        studentService = new StudentServiceImpl(ioService);
    }

    @Test
    void whenDetermineCurrentStudent_thenSuccessful() {
        studentService.determineCurrentStudent();
        verify(ioService, times(2)).readStringWithPrompt(anyString());

    }
}
