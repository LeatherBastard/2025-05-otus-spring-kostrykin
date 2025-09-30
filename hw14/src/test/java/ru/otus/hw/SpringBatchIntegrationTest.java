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
class SpringBatchIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void testLibraryMigration() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());

        List<AuthorDocument> authors = mongoTemplate.findAll(AuthorDocument.class, "authors");
        assertEquals(3, authors.size());
        assertTrue(authors.stream().anyMatch(a -> "Author_1".equals(a.getFullName())));
        assertTrue(authors.stream().anyMatch(a -> "Author_2".equals(a.getFullName())));
        assertTrue(authors.stream().anyMatch(a -> "Author_3".equals(a.getFullName())));


        List<GenreDocument> genres = mongoTemplate.findAll(GenreDocument.class, "genres");
        assertEquals(6, genres.size());
        assertTrue(genres.stream().anyMatch(g -> "Genre_1".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Genre_2".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Genre_3".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Genre_4".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Genre_5".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Genre_6".equals(g.getName())));


        List<BookDocument> books = mongoTemplate.findAll(BookDocument.class, "books");
        assertEquals(3, books.size());

        BookDocument book = mongoTemplate.findOne(
                Query.query(Criteria.where("title").is("BookTitle_1")),
                BookDocument.class
        );
        assertNotNull(book);
        assertNotNull(book.getAuthor());
        assertEquals("Author_1", book.getAuthor().getFullName());
        assertEquals(2, book.getGenres().size());
        assertEquals("Genre_1", book.getGenres().get(0).getName());
        assertEquals("Genre_2", book.getGenres().get(1).getName());


        List<CommentDocument> comments = mongoTemplate.findAll(CommentDocument.class, "comments");
        assertEquals(3, comments.size());

        BookDocument bookWithComments = mongoTemplate.findOne(
                Query.query(Criteria.where("title").is("BookTitle_2")),
                BookDocument.class
        );
        assertNotNull(bookWithComments);

        List<CommentDocument> bookComments = mongoTemplate.find(
                Query.query(Criteria.where("book").is(bookWithComments.getId())),
                CommentDocument.class
        );
        assertEquals(1, bookComments.size());
        assertEquals("Nice book", bookComments.get(0).getText());
    }


}

