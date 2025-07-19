package ru.otus.hw.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dto.QuestionDao;

import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private final IOService ioService = Mockito.mock(IOService.class);


    private final QuestionDao questionDao = Mockito.mock(QuestionDao.class);


    private TestService testService;

    @BeforeEach
    void initialize() {
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void whenExecuteTest_thenSuccessful() {
        testService.executeTest();
        verify(ioService, atLeast(1)).printLine(anyString());
        verify(ioService, atLeast(1)).printFormattedLine(anyString());
        verify(questionDao).findAll();
    }

}
