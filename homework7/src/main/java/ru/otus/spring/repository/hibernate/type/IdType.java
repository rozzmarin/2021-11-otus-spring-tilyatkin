package ru.otus.spring.repository.hibernate.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public abstract class IdType<T extends Serializable> implements UserType, ParameterizedType, ResultSetIdentifierConsumer {
    private final Class<T> typeClass;
    protected String idColumnName = "ID";

    public IdType(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    public abstract T get(ResultSet resultSet, String[] strings) throws SQLException;

    public abstract void set(PreparedStatement preparedStatement, T id, int index) throws SQLException;

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        return get(resultSet, strings);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        set(preparedStatement, (T)o, i);
    }

    @Override
    public Class returnedClass() {
        return typeClass;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return Objects.equals(o, o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        return o;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable)o;
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return serializable;
    }

    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return o;
    }

    @Override
    public void setParameterValues(Properties var1) {
        idColumnName = var1.getProperty("idColumn");
    }

    @Override
    public Serializable consumeIdentifier(ResultSet resultSet) {
        try {
            return get(resultSet, new String[] { idColumnName });
        } catch (SQLException e) {
            throw new IdentifierGenerationException("Error converting type", e);
        }
    }
}
