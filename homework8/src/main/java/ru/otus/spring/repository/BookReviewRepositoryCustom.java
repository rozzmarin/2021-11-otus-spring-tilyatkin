package ru.otus.spring.repository;

import ru.otus.spring.domain.BookId;

import java.util.Map;
import java.util.Set;

public interface BookReviewRepositoryCustom {
    Map<BookId, Long> countAtBooks(Set<BookId> bookIds);
}
