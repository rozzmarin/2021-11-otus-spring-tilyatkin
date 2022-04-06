package ru.otus.spring.dao.jdbc.context;

import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.*;
import ru.otus.spring.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommonContext {
    private final NamedParameterJdbcOperations jdbc;

    public long getAuthorsCount(AuthorFilter filter) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        sql.append("\nSELECT");
        sql.append("\n  COUNT(1)");
        sql.append("\nFROM AUTHOR");
        sql.append("\nWHERE 1 = 1");
        createAuthorFilter(sql, parameterSource, filter);
        return jdbc.queryForObject(sql.toString(), parameterSource, long.class);
    }

    public List<Author> getAuthors(AuthorFilter filter) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        sql.append("\nSELECT");
        sql.append("\n  AUTHOR_ID,");
        sql.append("\n  LASTNAME,");
        sql.append("\n  FIRSTNAME");
        sql.append("\nFROM AUTHOR");
        sql.append("\nWHERE 1 = 1");
        createAuthorFilter(sql, parameterSource, filter);
        return jdbc.query(sql.toString(), parameterSource, new AuthorMapper());
    }

    public List<Pair<BookId, Author>> getBookAuthors(List<BookId> bookIds) {
        if (bookIds.size() == 0) {
            throw new IllegalArgumentException("Empty book id list");
        }

        StringBuilder sql = new StringBuilder();
        String bookIdsString = bookIds
                .stream()
                .map(b -> Long.toString(b.getBookId()))
                .collect(Collectors.joining(","));
        sql.append("\nSELECT");
        sql.append("\n  ba.BOOK_ID,");
        sql.append("\n  a.AUTHOR_ID,");
        sql.append("\n  a.LASTNAME,");
        sql.append("\n  a.FIRSTNAME");
        sql.append("\nFROM AUTHOR a");
        sql.append("\n   INNER JOIN BOOK_AUTHOR ba ON ba.AUTHOR_ID = a.AUTHOR_ID");
        sql.append("\nWHERE ba.BOOK_ID IN");
        sql.append("\n  (");
        sql.append(bookIdsString);
        sql.append("\n  )");
        return jdbc.query(sql.toString(), new BookAuthorMapper());
    }

    public AuthorId insertAuthor(Author author) {
        if (author.getAuthorId() != null) {
            throw new IllegalArgumentException("Cannot insert item with non empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nINSERT INTO AUTHOR (LASTNAME, FIRSTNAME)")
                .append("\nVALUES (:LASTNAME, :FIRSTNAME)");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("LASTNAME", author.getLastname())
                .addValue("FIRSTNAME", author.getFirstname());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql.toString(), parameterSource, keyHolder);
        return new AuthorId((long)keyHolder.getKey());
    }

    public AuthorId updateAuthor(Author author) {
        if (author.getAuthorId() == null) {
            throw new IllegalArgumentException("Cannot update item with empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nUPDATE AUTHOR SET")
                .append("\n  LASTNAME = :LASTNAME,")
                .append("\n  FIRSTNAME = :FIRSTNAME")
                .append("\nWHERE AUTHOR_ID = :AUTHOR_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("AUTHOR_ID", author.getAuthorId().getAuthorId())
                .addValue("LASTNAME", author.getLastname())
                .addValue("FIRSTNAME", author.getFirstname());
        if (jdbc.update(sql.toString(), parameterSource) == 0) {
            return null;
        }
        return author.getAuthorId();
    }

    public AuthorId deleteAuthor(AuthorId id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot delete item with empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nDELETE")
                .append("\nFROM AUTHOR")
                .append("\nWHERE AUTHOR_ID = :AUTHOR_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("AUTHOR_ID", id.getAuthorId());
        if (jdbc.update(sql.toString(), parameterSource) == 0) {
            return null;
        }
        return id;
    }

    public long getGenresCount(GenreFilter filter) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        sql.append("\nSELECT");
        sql.append("\n  COUNT(1)");
        sql.append("\nFROM GENRE");
        sql.append("\nWHERE 1 = 1");
        createGenreFilter(sql, parameterSource, filter);
        return jdbc.queryForObject(sql.toString(), parameterSource, long.class);
    }

    public List<Genre> getGenres(GenreFilter filter) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        sql.append("\nSELECT");
        sql.append("\n  GENRE_ID,");
        sql.append("\n  TITLE");
        sql.append("\nFROM GENRE");
        sql.append("\nWHERE 1 = 1");
        createGenreFilter(sql, parameterSource, filter);
        return jdbc.query(sql.toString(), parameterSource, new GenreMapper());
    }

    public List<Pair<BookId, Genre>> getBookGenres(List<BookId> bookIds) {
        if (bookIds.size() == 0) {
            throw new IllegalArgumentException("Empty book id list");
        }

        StringBuilder sql = new StringBuilder();
        String bookIdsString = bookIds
                .stream()
                .map(b -> Long.toString(b.getBookId()))
                .collect(Collectors.joining(","));
        sql.append("\nSELECT");
        sql.append("\n  bg.BOOK_ID,");
        sql.append("\n  g.GENRE_ID,");
        sql.append("\n  g.TITLE");
        sql.append("\nFROM GENRE g");
        sql.append("\n   INNER JOIN BOOK_GENRE bg ON bg.GENRE_ID = g.GENRE_ID");
        sql.append("\nWHERE bg.BOOK_ID IN");
        sql.append("\n  (");
        sql.append(bookIdsString);
        sql.append("\n  )");
        return jdbc.query(sql.toString(), new BookGenreMapper());
    }

    public GenreId insertGenre(Genre genre) {
        if (genre.getGenreId() != null) {
            throw new IllegalArgumentException("Cannot insert item with non empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nINSERT INTO GENRE (TITLE)")
                .append("\nVALUES (:TITLE)");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("TITLE", genre.getTitle());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql.toString(), parameterSource, keyHolder);
        return new GenreId((long)keyHolder.getKey());
    }

    public GenreId updateGenre(Genre genre) {
        if (genre.getGenreId() == null) {
            throw new IllegalArgumentException("Cannot update item with empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nUPDATE GENRE SET")
                .append("\n  TITLE = :TITLE")
                .append("\nWHERE GENRE_ID = :GENRE_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("GENRE_ID", genre.getGenreId().getGenreId())
                .addValue("TITLE", genre.getTitle());
        if (jdbc.update(sql.toString(), parameterSource) == 0) {
            return null;
        }
        return genre.getGenreId();
    }

    public GenreId deleteGenre(GenreId id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot delete item with empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nDELETE")
                .append("\nFROM GENRE")
                .append("\nWHERE GENRE_ID = :GENRE_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("GENRE_ID", id.getGenreId());
        if (jdbc.update(sql.toString(), parameterSource) == 0) {
            return null;
        }
        return id;
    }

    public long getBooksCount(BookFilter filter) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        sql.append("\nSELECT");
        sql.append("\n  COUNT(DISTINCT BOOK.BOOK_ID)");
        sql.append("\nFROM BOOK");
        if (filter.isAuthorFilterSpecified()) {
            sql.append("\n  INNER JOIN BOOK_AUTHOR ON BOOK_AUTHOR.BOOK_ID = BOOK.BOOK_ID");
            sql.append("\n  INNER JOIN AUTHOR ON AUTHOR.AUTHOR_ID = BOOK_AUTHOR.AUTHOR_ID");
        }
        if (filter.isGenreFilterSpecified()) {
            sql.append("\n  INNER JOIN BOOK_GENRE ON BOOK_GENRE.BOOK_ID = BOOK.BOOK_ID");
            sql.append("\n  INNER JOIN GENRE ON GENRE.GENRE_ID = BOOK_GENRE.GENRE_ID");
        }
        sql.append("\nWHERE 1 = 1");
        createBookFilter(sql, parameterSource, filter);
        if (filter.isAuthorFilterSpecified()) {
            createAuthorFilter(sql, parameterSource, filter.getAuthorFilter());
        }
        if (filter.isGenreFilterSpecified()) {
            createGenreFilter(sql, parameterSource, filter.getGenreFilter());
        }
        return jdbc.queryForObject(sql.toString(), parameterSource, long.class);
    }

    public List<Book> getBooks(BookFilter filter) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        sql.append("\nSELECT DISTINCT");
        sql.append("\n  BOOK.BOOK_ID,");
        sql.append("\n  BOOK.TITLE");
        sql.append("\nFROM BOOK");
        if (filter.isAuthorFilterSpecified()) {
            sql.append("\n  INNER JOIN BOOK_AUTHOR ON BOOK_AUTHOR.BOOK_ID = BOOK.BOOK_ID");
            sql.append("\n  INNER JOIN AUTHOR ON AUTHOR.AUTHOR_ID = BOOK_AUTHOR.AUTHOR_ID");
        }
        if (filter.isGenreFilterSpecified()) {
            sql.append("\n  INNER JOIN BOOK_GENRE ON BOOK_GENRE.BOOK_ID = BOOK.BOOK_ID");
            sql.append("\n  INNER JOIN GENRE ON GENRE.GENRE_ID = BOOK_GENRE.GENRE_ID");
        }
        sql.append("\nWHERE 1 = 1");
        createBookFilter(sql, parameterSource, filter);
        if (filter.isAuthorFilterSpecified()) {
            createAuthorFilter(sql, parameterSource, filter.getAuthorFilter());
        }
        if (filter.isGenreFilterSpecified()) {
            createGenreFilter(sql, parameterSource, filter.getGenreFilter());
        }
        return jdbc.query(sql.toString(), parameterSource, new BookMapper());
    }

    public BookId insertBook(Book book) {
        if (book.getBookId() != null) {
            throw new IllegalArgumentException("Cannot insert item with non empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nINSERT INTO BOOK (TITLE)")
                .append("\nVALUES (:TITLE)");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("TITLE", book.getTitle());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql.toString(), parameterSource, keyHolder);
        return new BookId((long)keyHolder.getKey());
    }

    public BookId updateBook(Book book) {
        if (book.getBookId() == null) {
            throw new IllegalArgumentException("Cannot update item with empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nUPDATE BOOK SET")
                .append("\n  TITLE = :TITLE")
                .append("\nWHERE BOOK_ID = :BOOK_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("BOOK_ID", book.getBookId().getBookId())
                .addValue("TITLE", book.getTitle());
        if (jdbc.update(sql.toString(), parameterSource) == 0) {
            return null;
        }
        return book.getBookId();
    }

    public BookId deleteBook(BookId id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot delete item with empty id");
        }

        StringBuilder sql = new StringBuilder()
                .append("\nDELETE")
                .append("\nFROM BOOK")
                .append("\nWHERE BOOK_ID = :BOOK_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("BOOK_ID", id.getBookId());
        if (jdbc.update(sql.toString(), parameterSource) == 0) {
            return null;
        }
        return id;
    }

    public void insertBookAuthor(Pair<BookId, AuthorId> bookAuthor) {
        StringBuilder sql = new StringBuilder()
                .append("\nINSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID)")
                .append("\nVALUES (:BOOK_ID, :AUTHOR_ID)");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("BOOK_ID", bookAuthor.getValue0().getBookId())
                .addValue("AUTHOR_ID", bookAuthor.getValue1().getAuthorId());
        jdbc.update(sql.toString(), parameterSource);
    }

    public void deleteBookAuthors(BookId bookId) {
        StringBuilder sql = new StringBuilder()
                .append("\nDELETE")
                .append("\nFROM BOOK_AUTHOR")
                .append("\nWHERE BOOK_ID = :BOOK_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("BOOK_ID", bookId.getBookId());
        jdbc.update(sql.toString(), parameterSource);
    }

    public void insertBookGenre(Pair<BookId, GenreId> bookGenre) {
        StringBuilder sql = new StringBuilder()
                .append("\nINSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID)")
                .append("\nVALUES (:BOOK_ID, :GENRE_ID)");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("BOOK_ID", bookGenre.getValue0().getBookId())
                .addValue("GENRE_ID", bookGenre.getValue1().getGenreId());
        jdbc.update(sql.toString(), parameterSource);
    }

    public void deleteBookGenres(BookId bookId) {
        StringBuilder sql = new StringBuilder()
                .append("\nDELETE")
                .append("\nFROM BOOK_GENRE")
                .append("\nWHERE BOOK_ID = :BOOK_ID");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("BOOK_ID", bookId.getBookId());
        jdbc.update(sql.toString(), parameterSource);
    }

    private void createAuthorFilter(StringBuilder sql, MapSqlParameterSource parameterSource, AuthorFilter filter) {
        if (filter.isAuthorIdsSpecified()) {
            String authorIdsString = filter.getAuthorIds()
                    .stream()
                    .map(id ->  Long.toString(id.getAuthorId()))
                    .collect(Collectors.joining(","));
            sql.append("\n  AND AUTHOR.AUTHOR_ID IN");
            sql.append("\n  (");
            sql.append(authorIdsString);
            sql.append("\n  )");
        }
        if (filter.isNameSpecified()) {
            sql.append("\n  AND (AUTHOR.LASTNAME LIKE :NAME OR AUTHOR.FIRSTNAME LIKE :NAME)");
            parameterSource.addValue("NAME", StringUtils.quoted(filter.getName(), '%'));
        }
    }

    private void createGenreFilter(StringBuilder sql, MapSqlParameterSource parameterSource, GenreFilter filter) {
        if (filter.isGenreIdsSpecified()) {
            String genreIdsString = filter.getGenreIds()
                    .stream()
                    .map(id ->  Long.toString(id.getGenreId()))
                    .collect(Collectors.joining(","));
            sql.append("\n  AND GENRE.GENRE_ID IN");
            sql.append("\n  (");
            sql.append(genreIdsString);
            sql.append("\n  )");
        }
        if (filter.isTitleSpecified()) {
            sql.append("\nAND GENRE.TITLE LIKE :TITLE");
            parameterSource.addValue("TITLE", StringUtils.quoted(filter.getTitle(), '%'));
        }
    }

    private void createBookFilter(StringBuilder sql, MapSqlParameterSource parameterSource, BookFilter filter) {
        if (filter.isBookIdsSpecified()) {
            String bookIdsString = filter.getBookIds()
                    .stream()
                    .map(id -> Long.toString(id.getBookId()))
                    .collect(Collectors.joining(","));
            sql.append("\n  AND BOOK.BOOK_ID IN");
            sql.append("\n  (");
            sql.append(bookIdsString);
            sql.append("\n  )");
        }
        if (filter.isTitleSpecified()) {
            sql.append("\n  AND BOOK.TITLE LIKE :TITLE");
            parameterSource.addValue("TITLE", StringUtils.quoted(filter.getTitle(), '%'));
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Author(
                    new AuthorId(rs.getLong("AUTHOR_ID")),
                    rs.getString("LASTNAME"),
                    rs.getString("FIRSTNAME")
            );
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(
                    new GenreId(rs.getLong("GENRE_ID")),
                    rs.getString("TITLE")
            );
        }
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    new BookId(rs.getLong("BOOK_ID")),
                    rs.getString("TITLE"),
                    Set.of(),
                    Set.of());
        }
    }

    private static class BookAuthorMapper implements RowMapper<Pair<BookId, Author>> {
        @Override
        public Pair<BookId, Author> mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Pair.with(
                    new BookId(rs.getLong("BOOK_ID")),
                    new Author(
                            new AuthorId(rs.getLong("AUTHOR_ID")),
                            rs.getString("LASTNAME"),
                            rs.getString("FIRSTNAME")));
        }
    }

    private static class BookGenreMapper implements RowMapper<Pair<BookId, Genre>> {
        @Override
        public Pair<BookId, Genre> mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Pair.with(
                    new BookId(rs.getLong("BOOK_ID")),
                    new Genre(
                            new GenreId(rs.getLong("GENRE_ID")),
                            rs.getString("TITLE")));
        }
    }
}
