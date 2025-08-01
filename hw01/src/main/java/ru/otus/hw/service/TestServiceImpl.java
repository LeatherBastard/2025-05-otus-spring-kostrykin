package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.dto.QuestionDao;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        questions.forEach((question) -> {
            ioService.printLine(question.text());
            int i = 1;
            for (Answer answer : question.answers()) {
                ioService.printFormattedLine(" %d: %s", i, answer.text());
                i++;
            }
        });
    }
}
