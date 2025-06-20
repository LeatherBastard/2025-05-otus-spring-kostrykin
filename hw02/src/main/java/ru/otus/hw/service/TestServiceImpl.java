package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below:%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine(question.text());
            int i = 1;
            for (Answer answer : question.answers()) {
                ioService.printFormattedLine(" %d: %s", i, answer.text());
                i++;
            }
            var answerChoice = ioService.readIntForRangeWithPrompt(
                    1,
                    questions.size(),
                    "Your choice:",
                    "There is no such answer!");
            var isAnswerValid = question.answers().get(answerChoice - 1).isCorrect(); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
