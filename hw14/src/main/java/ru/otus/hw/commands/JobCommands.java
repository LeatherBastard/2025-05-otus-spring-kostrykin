package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class JobCommands {
    private static final String JOB_NAME = "libraryMigrationJob";

    private final Job libraryMigrationJob;

    private final JobLauncher jobLauncher;

    private int jobTrial = 1;

    @ShellMethod(value = "startLibraryJobMigration", key = "sljm")
    public void startLibraryJobMigration() throws Exception {
        JobExecution execution = jobLauncher.run(libraryMigrationJob, new JobParametersBuilder()
                .addString("trial", String.valueOf(jobTrial))
                .toJobParameters());
        jobTrial++;
        System.out.println(execution);

    }
}
