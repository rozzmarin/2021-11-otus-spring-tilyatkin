package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Author {
    private final AuthorId authorId;
    private final String lastname;
    private final String firstname;

    public Author(AuthorId authorId) {
        this(authorId, null, null);
    }

    public Author(String lastname, String firstname) {
        this(null, lastname, firstname);
    }
}
