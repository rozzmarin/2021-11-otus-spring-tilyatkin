package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

public interface GenreDao {
    long count(GenreFilter filter);

    Genre get(GenreId id);

    List<Genre> get(GenreFilter filter);

    GenreId insert(Genre genre);

    GenreId update(Genre genre);

    GenreId delete(GenreId id);
}
