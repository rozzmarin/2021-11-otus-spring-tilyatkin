package ru.otus.spring.repository;

import ru.otus.spring.domain.BookId;
import ru.otus.spring.domain.BookReview;
import ru.otus.spring.domain.BookReviewFilter;
import ru.otus.spring.domain.BookReviewId;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookReviewRepository {
    long count(BookReviewFilter filter);

    Map<BookId, Long> countAtBooks(Set<BookId> bookIds);

    BookReview get(BookReviewId id);

    List<BookReview> get(BookReviewFilter filter);

    BookReviewId insert(BookReview bookReview);

    BookReviewId update(BookReview bookReview);

    BookReviewId delete(BookReviewId id);
}
