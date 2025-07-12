package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedMessagesService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest (classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {
    @MockitoBean
    private TestFileNameProvider testFileNameProvider;

    @MockitoBean(name = "localizedMessagesServiceImpl")
    private LocalizedMessagesService localizedMessagesService;

    @Autowired
    private CsvQuestionDao csvQuestionDao;


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
