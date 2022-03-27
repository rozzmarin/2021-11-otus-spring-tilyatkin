package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

public interface GenreService {
    Genre find(GenreId id);

    List<Genre> find(GenreFilter filter);

    Genre add(Genre genre);

    Genre edit(Genre genre);

    GenreId remove(GenreId id);
}
