package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class AuthorId implements Serializable {
    private final String authorId;

    @Override
    public String toString() {
        return authorId;
    }

}
