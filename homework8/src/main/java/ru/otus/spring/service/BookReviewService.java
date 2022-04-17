package ru.otus.spring.service;

import ru.otus.spring.domain.BookReview;
import ru.otus.spring.domain.BookReviewFilter;
import ru.otus.spring.domain.BookReviewId;

import java.util.List;

public interface BookReviewService {
    BookReview find(BookReviewId id);

    List<BookReview> find(BookReviewFilter filter);

    BookReview add(BookReview bookReview);

    BookReview edit(BookReview bookReview);

    BookReviewId remove(BookReviewId id);
}
