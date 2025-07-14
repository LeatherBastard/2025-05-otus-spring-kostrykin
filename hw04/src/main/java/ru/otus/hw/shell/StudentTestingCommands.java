package ru.otus.hw.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent("Student Testing Commands")
public class StudentTestingCommands {

    private Student student;

    private TestResult testResult;

    private final LocalizedIOService ioService;

    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    public StudentTestingCommands(LocalizedIOService ioService,
                                  StudentService studentService,
                                  TestService testService,
                                  ResultService resultService) {
        this.ioService = ioService;
        this.studentService = studentService;
        this.testService = testService;
        this.resultService = resultService;
    }

    @ShellMethod(value = "Login command", key = {"l", "log", "login"})
    public void login() {
        student = studentService.determineCurrentStudent();
        ioService.printFormattedLineLocalized("StudentTestingCommands.output.logged",
                student.firstName() + " " + student.lastName());
    }

    @ShellMethod(value = "Start test command", key = {"st", "test", "start test"})
    @ShellMethodAvailability(value = "isExecuteTestCommandAvailable")
    public void executeTest() {
        testResult = testService.executeTestFor(student);
    }

    @ShellMethod(value = "Show test result command", key = {"result", "show results"})
    @ShellMethodAvailability(value = "isShowTestResultCommandAvailable")
    public void showResult() {
        resultService.showResult(testResult);
    }

    private Availability isExecuteTestCommandAvailable() {
        return student != null
                ? Availability.available()
                : Availability.unavailable(ioService.getMessage("StudentTestingCommands.start.test.availability"));
    }

    private Availability isShowTestResultCommandAvailable() {
        return testResult != null
                ? Availability.available()
                : Availability.unavailable(ioService.getMessage("StudentTestingCommands.show.result.availability"));
    }
}
