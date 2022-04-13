package ru.otus.spring.repository.hibernate.type;

import ru.otus.spring.domain.BookReviewId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class BookReviewIdType extends IdType<BookReviewId> {
    public BookReviewIdType() {
        super(BookReviewId.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.BIGINT };
    }

    @Override
    public BookReviewId get(ResultSet resultSet, String[] strings) throws SQLException {
        long id = resultSet.getLong(strings[0]);
        if (resultSet.wasNull()) {
            return null;
        }
        return new BookReviewId(id);
    }

    @Override
    public void set(PreparedStatement preparedStatement, BookReviewId id, int index) throws SQLException {
        if (id == null) {
            preparedStatement.setNull(index, Types.BIGINT);
        } else {
            preparedStatement.setLong(index, id.getBookReviewId());
        }
    }
}
