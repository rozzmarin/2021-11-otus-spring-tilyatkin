package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class GenreId implements Serializable {
    private final long genreId;
}
