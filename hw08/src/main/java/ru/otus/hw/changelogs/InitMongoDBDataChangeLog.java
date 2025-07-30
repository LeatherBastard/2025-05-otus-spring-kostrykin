package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private List<Author> authors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "lb", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "lb", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        authors.add(repository.save(new Author("Author_1")));
        authors.add(repository.save(new Author("Author_2")));
        authors.add(repository.save(new Author("Author_3")));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "lb", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genres.add(repository.save(new Genre("Genre_1")));
        genres.add(repository.save(new Genre("Genre_2")));
        genres.add(repository.save(new Genre("Genre_3")));
        genres.add(repository.save(new Genre("Genre_4")));
        genres.add(repository.save(new Genre("Genre_5")));
        genres.add(repository.save(new Genre("Genre_6")));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "lb", runAlways = true)
    public void initBooks(BookRepository repository) {
        books.add(repository.save(
                new Book("BookTitle_1", authors.get(0), List.of(genres.get(0), genres.get(1)))));
        books.add(repository.save(
                new Book("BookTitle_2", authors.get(1), List.of(genres.get(2), genres.get(3)))));
        books.add(repository.save(
                new Book("BookTitle_3", authors.get(2), List.of(genres.get(4), genres.get(5)))));
    }

    @ChangeSet(order = "004", id = "initComments", author = "lb", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.save(new Comment(books.get(1), "Nice book"));
        repository.save(new Comment(books.get(2), "Very good"));
        repository.save(new Comment(books.get(2), "Boring"));
    }
}
