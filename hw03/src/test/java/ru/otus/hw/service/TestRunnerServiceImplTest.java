package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;

class TestRunnerServiceImplTest {
    private final TestService testService = Mockito.mock(TestService.class);

    private final StudentService studentService = Mockito.mock(StudentService.class);

    private final ResultService resultService = Mockito.mock(ResultService.class);

    private CommandLineRunner testRunnerService;

    @BeforeEach
    void initialize() {
        testRunnerService = new CommandLineRunnerImpl(testService, studentService, resultService);
    }

    @Test
    void whenRun_thenSuccessful() throws Exception {
        testRunnerService.run();
        InOrder order = inOrder(studentService, testService, resultService);
        order.verify(studentService).determineCurrentStudent();
        order.verify(testService).executeTestFor(any());
        order.verify(resultService).showResult(any());
    }
}
