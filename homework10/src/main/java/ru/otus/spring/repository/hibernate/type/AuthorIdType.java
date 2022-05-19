package ru.otus.spring.repository.hibernate.type;

import ru.otus.spring.domain.AuthorId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AuthorIdType extends IdType<AuthorId> {
    public AuthorIdType() {
        super(AuthorId.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.BIGINT };
    }

    @Override
    public AuthorId get(ResultSet resultSet, String[] strings) throws SQLException {
        long id = resultSet.getLong(strings[0]);
        if (resultSet.wasNull()) {
            return null;
        }
        return new AuthorId(id);
    }

    @Override
    public void set(PreparedStatement preparedStatement, AuthorId id, int index) throws SQLException {
        if (id == null) {
            preparedStatement.setNull(index, Types.BIGINT);
        } else {
            preparedStatement.setLong(index, id.getAuthorId());
        }
    }
}
