package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CsvQuestionDaoIntegrationTest {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void initialize() {
        appProperties.setLocale("en-US");
    }

    @Test
    void whenFindAll_thenSuccessful() {
        List<Question> expectedQuestions = List.of(
                new Question("Is there life on Mars?",
                        List.of(
                                new Answer("Science doesn't know this yet", true),
                                new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                                new Answer("Absolutely not", false)
                        )),
                new Question("How should resources be loaded form jar in Java?",
                        List.of(
                                new Answer("ClassLoader#geResourceAsStream or ClassPathResource#getInputStream", true),
                                new Answer("ClassLoader#geResource#getFile + FileReader", false),
                                new Answer("Wingardium Leviosa", false)
                        )),
                new Question("Which option is a good way to handle the exception?",
                        List.of(
                                new Answer("@SneakyThrow", false),
                                new Answer("e.printStackTrace()", false),
                                new Answer("Rethrow with wrapping in business exception (for example, QuestionReadException)", true),
                                new Answer("Ignoring exception", false)
                        )));
        List<Question> actualQuestions = csvQuestionDao.findAll();
        assertEquals(expectedQuestions, actualQuestions);

    }
}
