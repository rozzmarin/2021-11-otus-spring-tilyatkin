package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class BookId implements Serializable {
    private final String bookId;

    @Override
    public String toString() {
        return bookId;
    }
}
