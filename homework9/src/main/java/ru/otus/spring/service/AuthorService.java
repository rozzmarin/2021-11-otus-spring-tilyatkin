package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

public interface AuthorService {
    Author find(AuthorId id);

    List<Author> find(AuthorFilter filter);

    Author add(Author author);

    Author edit(Author author);

    AuthorId remove(AuthorId id);
}
