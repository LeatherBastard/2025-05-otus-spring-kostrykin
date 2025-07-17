package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Author> findAll() {
        return jdbcOperations.query("SELECT * FROM authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        Map<String, Object> params = Map.of("id", id);
        List<Author> authors = jdbcOperations.
                query("SELECT * FROM authors WHERE id = :id", params, new AuthorResultSetExtractor());
        return authors.isEmpty() ? Optional.empty() : Optional.of(authors.get(0));
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return new Author(
                    rs.getLong("id"),
                    rs.getString("full_name"));
        }
    }

    @RequiredArgsConstructor
    private static class AuthorResultSetExtractor implements ResultSetExtractor<List<Author>> {
        @Override
        public List<Author> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Author> authors = new ArrayList<>();
            while (rs.next()) {
                Author author = new Author(
                        rs.getLong("id"),
                        rs.getString("full_name"));
                authors.add(author);
            }
            return authors;
        }
    }
}
