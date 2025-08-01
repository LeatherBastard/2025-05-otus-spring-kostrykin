package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.dto.QuestionDao;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine(question.text());
            int i = 1;
            for (Answer answer : question.answers()) {
                ioService.printFormattedLine(" %d: %s", i, answer.text());
                i++;
            }
            var answerChoice = ioService.readIntForRangeWithPromptLocalized(
                    1,
                    questions.size(),
                    "TestService.choose.the.question",
                    "TestService.wrong.question");
            var isAnswerValid = question.answers().get(answerChoice - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
