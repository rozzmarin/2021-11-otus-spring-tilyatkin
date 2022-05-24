package ru.otus.spring.repository.hibernate.type;

import ru.otus.spring.domain.GenreId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class GenreIdType extends IdType<GenreId> {
    public GenreIdType() {
        super(GenreId.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.BIGINT };
    }

    @Override
    public GenreId get(ResultSet resultSet, String[] strings) throws SQLException {
        long id = resultSet.getLong(strings[0]);
        if (resultSet.wasNull()) {
            return null;
        }
        return new GenreId(id);
    }

    @Override
    public void set(PreparedStatement preparedStatement, GenreId id, int index) throws SQLException {
        if (id == null) {
            preparedStatement.setNull(index, Types.BIGINT);
        } else {
            preparedStatement.setLong(index, id.getGenreId());
        }
    }
}
