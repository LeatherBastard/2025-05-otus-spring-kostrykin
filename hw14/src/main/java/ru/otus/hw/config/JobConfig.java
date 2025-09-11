package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.models.mongo.GenreDocument;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor

public class JobConfig {
    private static final int CHUNK_SIZE = 10;

    private final JobRepository jobRepository;

    private final EntityManagerFactory entityManagerFactory;


    private final MongoTemplate mongoTemplate;

    private final PlatformTransactionManager platformTransactionManager;

    private final ItemProcessor<Author, AuthorDocument> authorProcessor;

    private final ItemProcessor<Genre, GenreDocument> genreProcessor;

    private final ItemProcessor<Book, BookDocument> bookProcessor;

    private final ItemProcessor<Comment, CommentDocument> commentProcessor;


    @Bean
    public JpaCursorItemReader<Author> authorReader() {
        return new JpaCursorItemReaderBuilder<Author>()
                .name("authorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT a FROM Author a")
                .saveState(true)
                .build();
    }

    @Bean
    public JpaCursorItemReader<Genre> genreReader() {
        return new JpaCursorItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT g FROM Genre g")
                .saveState(true)
                .build();
    }

    @Bean
    public JpaCursorItemReader<Book> bookReader() {
        return new JpaCursorItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT b FROM Book b")
                .saveState(true)
                .build();
    }

    @Bean
    public JpaCursorItemReader<Comment> commentReader() {
        return new JpaCursorItemReaderBuilder<Comment>()
                .name("commentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c FROM Comment c")
                .saveState(true)
                .build();
    }


    @Bean
    public MongoItemWriter<AuthorDocument> authorWriter() {
        MongoItemWriter<AuthorDocument> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("authors");
        return writer;
    }

    @Bean
    public MongoItemWriter<GenreDocument> genreWriter() {
        MongoItemWriter<GenreDocument> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("genres");
        return writer;
    }

    @Bean
    public MongoItemWriter<BookDocument> bookWriter() {
        MongoItemWriter<BookDocument> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("books");
        return writer;
    }

    @Bean
    public MongoItemWriter<CommentDocument> commentWriter() {
        MongoItemWriter<CommentDocument> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("comments");
        return writer;
    }


    @Bean
    public Step migrateAuthorsStep() {
        return new StepBuilder("migrateAuthorStep", jobRepository)
                .<Author, AuthorDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorReader())
                .processor(authorProcessor)
                .writer(authorWriter())
                .build();
    }

    @Bean
    public Step migrateGenresStep() {
        return new StepBuilder("migrateGenresStep", jobRepository)
                .<Genre, GenreDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(genreReader())
                .processor(genreProcessor)
                .writer(genreWriter())
                .build();
    }

    @Bean
    public Step migrateBooksStep() {
        return new StepBuilder("migrateBooksStep", jobRepository)
                .<Book, BookDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookReader())
                .processor(bookProcessor)
                .writer(bookWriter())
                .build();
    }

    @Bean
    public Step migrateCommentsStep() {
        return new StepBuilder("migrateCommentsStep", jobRepository)
                .<Comment, CommentDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentReader())
                .processor(commentProcessor)
                .writer(commentWriter())
                .build();
    }


    @Bean
    public Job libraryMigrationJob() {
        return new JobBuilder("libraryMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrateAuthorsStep())
                .next(migrateGenresStep())
                .next(migrateBooksStep())
                .next(migrateCommentsStep())
                .build();
    }


}
