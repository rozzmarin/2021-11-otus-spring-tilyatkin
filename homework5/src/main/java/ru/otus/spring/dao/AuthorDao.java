package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

public interface AuthorDao {
    long count(AuthorFilter filter);

    Author get(AuthorId id);

    List<Author> get(AuthorFilter filter);

    AuthorId insert(Author author);

    AuthorId update(Author author);

    AuthorId delete(AuthorId id);
}
