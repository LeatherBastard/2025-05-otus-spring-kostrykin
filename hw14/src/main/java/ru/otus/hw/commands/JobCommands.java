package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Properties;

@RequiredArgsConstructor
@ShellComponent
public class JobCommands {

    private final Job libraryMigrationJob;

    private final JobOperator jobOperator;

    private static final String JOB_NAME = "libraryMigrationJob";

    @ShellMethod(value = "startLibraryJobMigration", key = "sljm")
    public void startLibraryJobMigration() throws Exception {
        Long executionId = jobOperator.start(JOB_NAME, new Properties());
        System.out.println(jobOperator.getSummary(executionId));

    }
}
