package ru.otus.hw.config;


import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongockConfig {
    @Bean
    public MongockApplicationRunner mongockApplicationRunner(
            ApplicationContext springContext,
            MongoClient mongoClient,
            ReactiveMongoTemplate reactiveMongoTemplate) {

        return MongockSpringboot.builder()
                .setDriver(MongoReactiveDriver.withDefaultLock(mongoClient, "library"))
                .addMigrationScanPackage("ru.otus.hw.changelogs")
                .setSpringContext(springContext)
                .setTransactionEnabled(false)
                .buildApplicationRunner();
    }
}
