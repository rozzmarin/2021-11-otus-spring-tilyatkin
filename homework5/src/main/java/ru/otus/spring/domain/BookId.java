package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@RequiredArgsConstructor
@Data
public class BookId {
    private final long bookId;
}
