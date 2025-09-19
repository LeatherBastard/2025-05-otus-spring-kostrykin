package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.models.mongo.GenreDocument;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBatchTest
@SpringBootTest
public class SpringBatchIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testLibraryMigration() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // Проверка статуса выполнения
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());

        // Проверка миграции авторов
        List<AuthorDocument> authors = mongoTemplate.findAll(AuthorDocument.class, "authors");
        assertEquals(2, authors.size());
        assertTrue(authors.stream().anyMatch(a -> "Лев Толстой".equals(a.getFullName())));
        assertTrue(authors.stream().anyMatch(a -> "Фёдор Достоевский".equals(a.getFullName())));

        // Проверка миграции жанров
        List<GenreDocument> genres = mongoTemplate.findAll(GenreDocument.class, "genres");
        assertEquals(3, genres.size());
        assertTrue(genres.stream().anyMatch(g -> "Роман".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Классика".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Философия".equals(g.getName())));

        // Проверка миграции книг
        List<BookDocument> books = mongoTemplate.findAll(BookDocument.class, "books");
        assertEquals(2, books.size());

        BookDocument warAndPeace = mongoTemplate.findOne(
                Query.query(Criteria.where("title").is("Война и мир")),
                BookDocument.class
        );
        assertNotNull(warAndPeace);
        assertNotNull(warAndPeace.getAuthor());
        assertEquals("Лев Толстой", warAndPeace.getAuthor().getFullName());
        assertEquals(2, warAndPeace.getGenres().size());

        // Проверка миграции комментариев
        List<CommentDocument> comments = mongoTemplate.findAll(CommentDocument.class, "comments");
        assertEquals(3, comments.size());

        // Проверка связей между документами
        BookDocument crimeAndPunishment = mongoTemplate.findOne(
                Query.query(Criteria.where("title").is("Преступление и наказание")),
                BookDocument.class
        );
        assertNotNull(crimeAndPunishment);

        List<CommentDocument> bookComments = mongoTemplate.find(
                Query.query(Criteria.where("book.$id").is(crimeAndPunishment.getId())),
                CommentDocument.class
        );
        assertEquals(1, bookComments.size());
        assertEquals("Глубокое произведение", bookComments.get(0).getText());
    }




}

