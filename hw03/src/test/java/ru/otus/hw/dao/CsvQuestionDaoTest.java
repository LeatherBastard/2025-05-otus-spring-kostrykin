package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedMessagesService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {
    private final TestFileNameProvider testFileNameProvider = Mockito.mock(TestFileNameProvider.class);

    private final LocalizedMessagesService localizedMessagesService = Mockito.mock(LocalizedMessagesService.class);
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void initialize() {
        csvQuestionDao = new CsvQuestionDao(testFileNameProvider, localizedMessagesService);
    }

    @Test
    void whenFindAll_thenSuccessful() {
        when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");
        List<Question> answers = csvQuestionDao.findAll();
        assertNotNull(answers);
        assertFalse(answers.isEmpty());
    }

    @Test
    void whenFindAll_thenQuestionReadException() {
        when(testFileNameProvider.getTestFileName()).thenReturn("abc");
        assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());
    }

}
