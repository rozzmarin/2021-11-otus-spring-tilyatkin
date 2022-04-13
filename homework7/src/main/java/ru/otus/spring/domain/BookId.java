package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class BookId implements Serializable, Comparable<BookId> {
    private final long bookId;

    @Override
    public int compareTo(BookId o) {
        return Long.compare(this.bookId, o.bookId);
    }
}
