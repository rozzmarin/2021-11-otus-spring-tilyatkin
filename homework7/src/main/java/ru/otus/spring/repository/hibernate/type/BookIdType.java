package ru.otus.spring.repository.hibernate.type;

import ru.otus.spring.domain.BookId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class BookIdType extends IdType<BookId> {
    public BookIdType() {
        super(BookId.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.BIGINT };
    }

    @Override
    public BookId get(ResultSet resultSet, String[] strings) throws SQLException {
        long id = resultSet.getLong(strings[0]);
        if (resultSet.wasNull()) {
            return null;
        }
        return new BookId(id);
    }

    @Override
    public void set(PreparedStatement preparedStatement, BookId id, int index) throws SQLException {
        if (id == null) {
            preparedStatement.setNull(index, Types.BIGINT);
        } else {
            preparedStatement.setLong(index, id.getBookId());
        }
    }
}
