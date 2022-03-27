package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Data
public class Book {
    private final BookId bookId;
    private final String title;
    private final Set<Author> authors;
    private final Set<Genre> genres;

    public Book(String title, Set<Author> authors, Set<Genre> genres) {
        this(null, title, authors, genres);
    }
}
