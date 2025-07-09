package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private static final String BOOK_BY_ID_NOT_FOUND = "Book with id %d not found";

    private final NamedParameterJdbcOperations jdbcOperations;

    private final GenreRepository genreRepository;


    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Map.of("id", id);
        Optional<Book> optionalBook = Optional.ofNullable(
                jdbcOperations.query("SELECT books.id,title,author_id, full_name FROM books " +
                                "JOIN authors ON books.author_id=authors.id WHERE books.id= :id;",
                        params,
                        new BookResultSetExtractor()));

        if (optionalBook.isPresent()) {
            Set<Long> bookGenres = getAllGenreIdsByBook(optionalBook.get().getId());
            List<Genre> genres = genreRepository.findAllByIds(bookGenres);
            optionalBook.get().setGenres(genres);
        }

        return optionalBook;
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Map.of("book_id", id);
        int updatedRecords = jdbcOperations.update("DELETE FROM books WHERE id=:book_id", params);

        if (updatedRecords == 0) {
            throw new EntityNotFoundException(String.format(BOOK_BY_ID_NOT_FOUND, id));
        }
        //...
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbcOperations.query("SELECT books.id, title,author_id, full_name FROM books " +
                "JOIN authors ON books.author_id=authors.id;", new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcOperations.query("SELECT * FROM books_genres", rs -> {
            List<BookGenreRelation> bookGenreRelationList = new ArrayList<>();
            while (rs.next()) {
                BookGenreRelation bookGenreRelation = new BookGenreRelation(
                        rs.getLong("book_id"),
                        rs.getLong("genre_id")
                );
                bookGenreRelationList.add(bookGenreRelation);
            }
            return bookGenreRelationList;
        });
    }

    private Set<Long> getAllGenreIdsByBook(long bookId) {
        Map<String, Object> params = Map.of("book_id", bookId);
        return jdbcOperations.query("SELECT genre_id FROM books_genres WHERE book_id=:book_id", params, rs -> {
            Set<Long> bookGenreIds = new HashSet<>();
            while (rs.next()) {
                Long genreId = rs.getLong("genre_id");
                bookGenreIds.add(genreId);
            }
            return bookGenreIds;
        });
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        for (BookGenreRelation bookGenreRelation : relations) {
            long bookId = (int) bookGenreRelation.bookId;
            Book foundBook = null;
            for (Book book : booksWithoutGenres) {
                if (book.getId() == bookId) {
                    foundBook = book;
                    break;
                }
            }
            long genreId = (int) bookGenreRelation.genreId;
            Genre foundGenre = null;
            for (Genre genre : genres) {
                if (genre.getId() == genreId) {
                    foundGenre = genre;
                    break;
                }
            }
            if (foundBook != null && foundGenre != null) {
                foundBook.getGenres().add(foundGenre);
            }
        }
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        //...
        jdbcOperations.update("INSERT INTO books(title,author_id) VALUES (:title,:author_id)"
                , params, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        //...
        Map<String, Object> params =
                Map.of("title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "book_id", book.getId());
        int updatedRecords = jdbcOperations.update(
                "UPDATE books SET title=:title,author_id=:author_id WHERE id=:book_id", params);
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        if (updatedRecords == 0) {
            throw new EntityNotFoundException(String.format(BOOK_BY_ID_NOT_FOUND, book.getId()));
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        // Использовать метод batchUpdate
        Map<String, Object>[] params = new Map[book.getGenres().size()];
        for (int i = 0; i < book.getGenres().size(); i++) {
            params[i] = Map.of("book_id", book.getId(), "genre_id", book.getGenres().get(i).getId());
        }
        jdbcOperations.batchUpdate("INSERT INTO books_genres VALUES(:book_id,:genre_id)", params);
    }


    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> params = Map.of("book_id", book.getId());
        int updatedRecords = jdbcOperations.update("DELETE FROM books_genres WHERE book_id=:book_id", params);
        if (updatedRecords == 0) {
            throw new EntityNotFoundException(String.format(BOOK_BY_ID_NOT_FOUND, book.getId()));
        }
        //...
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return
                    new Book(rs.getLong("id")
                            , rs.getString("title")
                            , new Author(rs.getLong("author_id"), rs.getString("full_name"))
                            , new ArrayList<>());
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return new Book(rs.getLong("id")
                        , rs.getString("title")
                        , new Author(rs.getLong("author_id"), rs.getString("full_name"))
                        , null);
            }
            return null;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
