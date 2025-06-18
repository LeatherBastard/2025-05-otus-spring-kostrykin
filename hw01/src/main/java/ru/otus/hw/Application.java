package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    private static final String XML_CONTEXT_PATH = "spring-context.xml";

    public static void main(String[] args) {

        //Прописать бины в spring-context.xml и создать контекст на его основе
        ApplicationContext context = new ClassPathXmlApplicationContext(XML_CONTEXT_PATH);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();


    }
}