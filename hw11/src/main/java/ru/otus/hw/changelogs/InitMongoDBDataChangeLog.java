package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeUnit(id = "init-mongodb-data", order = "001", author = "lb")
public class InitMongoDBDataChangeLog {

    @Execution
    public void execution(ReactiveMongoTemplate mongoTemplate,
                          AuthorRepository authorRepository,
                          GenreRepository genreRepository,
                          BookRepository bookRepository,
                          CommentRepository commentRepository) {

        clearData(mongoTemplate);

        initAuthors(authorRepository);
        initGenres(genreRepository);
        initBooks(bookRepository, authorRepository, genreRepository);
        initComments(commentRepository, bookRepository);

        System.out.println("All data initialized successfully");
    }

    @RollbackExecution
    public void rollback(ReactiveMongoTemplate mongoTemplate) {
        clearData(mongoTemplate);
        System.out.println("All data cleared during rollback");
    }

    private void clearData(ReactiveMongoTemplate mongoTemplate) {
        try {
            mongoTemplate.dropCollection("comments").block();
            mongoTemplate.dropCollection("books").block();
            mongoTemplate.dropCollection("authors").block();
            mongoTemplate.dropCollection("genres").block();
        } catch (Exception e) {
            System.out.println("Error clearing data: " + e.getMessage());
        }
    }

    private void initAuthors(AuthorRepository repository) {
        repository.save(new Author("Author_1")).block();
        repository.save(new Author("Author_2")).block();
        repository.save(new Author("Author_3")).block();
        System.out.println("Authors initialized");
    }

    private void initGenres(GenreRepository repository) {
        repository.save(new Genre("Genre_1")).block();
        repository.save(new Genre("Genre_2")).block();
        repository.save(new Genre("Genre_3")).block();
        repository.save(new Genre("Genre_4")).block();
        repository.save(new Genre("Genre_5")).block();
        repository.save(new Genre("Genre_6")).block();
        System.out.println("Genres initialized");
    }

    private void initBooks(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           GenreRepository genreRepository) {

        List<Author> authors = authorRepository.findAll().collectList().block();
        List<Genre> genres = genreRepository.findAll().collectList().block();

        if (authors != null && authors.size() >= 3 && genres != null && genres.size() >= 6) {
            bookRepository.save(new Book("BookTitle_1", authors.get(0),
                    List.of(genres.get(0), genres.get(1)))).block();

            bookRepository.save(new Book("BookTitle_2", authors.get(1),
                    List.of(genres.get(2), genres.get(3)))).block();

            bookRepository.save(new Book("BookTitle_3", authors.get(2),
                    List.of(genres.get(4), genres.get(5)))).block();

            System.out.println("Books initialized");
        }
    }

    private void initComments(CommentRepository commentRepository,
                              BookRepository bookRepository) {

        List<Book> books = bookRepository.findAll().collectList().block();

        if (books != null && books.size() >= 3) {
            commentRepository.save(new Comment(books.get(0).getId(), "Nice book")).block();
            commentRepository.save(new Comment(books.get(1).getId(), "Very good")).block();
            commentRepository.save(new Comment(books.get(2).getId(), "Boring")).block();
            commentRepository.save(new Comment(books.get(2).getId(), "Enjoying")).block();
            System.out.println("Comments initialized");
        }
    }
}