package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Genre {
    private final GenreId genreId;
    private final String title;

    public Genre(GenreId genreId) {
        this(genreId, null);
    }

    public Genre(String title) {
        this(null, title);
    }
}
