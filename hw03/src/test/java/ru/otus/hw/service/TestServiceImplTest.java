package ru.otus.hw.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private final LocalizedIOService ioService = Mockito.mock(LocalizedIOService.class);


    private final QuestionDao questionDao = Mockito.mock(QuestionDao.class);


    private TestService testService;

    @BeforeEach
    void initialize() {
        testService = new TestServiceImpl(ioService, questionDao);
    }


    @Test
    void whenExecuteTest_thenSuccessful() {
        Student student = new Student("Mark", "Kostrykin");
        List<Question> questions = List.of(
                new Question("What is the result of 2+2?",
                        List.of(new Answer("4", true), new Answer("5", false))));
        when(questionDao.findAll()).thenReturn(questions);
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);
        testService.executeTestFor(student);
        verify(ioService, atLeast(1)).printLine(anyString());
        verify(ioService, atLeast(1)).printLineLocalized(anyString());
        verify(ioService, atLeast(1)).readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString());
        verify(questionDao).findAll();
    }

}
