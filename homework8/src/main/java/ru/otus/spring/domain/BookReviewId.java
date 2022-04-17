package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class BookReviewId implements Serializable {
    private final String bookReviewId;

    @Override
    public String toString() {
        return bookReviewId;
    }
}
