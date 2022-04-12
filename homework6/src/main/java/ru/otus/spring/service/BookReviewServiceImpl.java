package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class BookReviewServiceImpl implements BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;


    @Override
    @Transactional(readOnly = true)
    public BookReview find(BookReviewId id) {
        return bookReviewRepository.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookReview> find(BookReviewFilter filter) {
        return bookReviewRepository.get(filter);
    }

    @Override
    public BookReview add(BookReview bookReview) {
        validateBookReview(bookReview);
        prepareBook(bookReview);
        BookReviewId bookReviewId = bookReviewRepository.insert(bookReview);
        if (bookReviewId != null) {
            return bookReviewRepository.get(bookReviewId);
        }
        return null;
    }

    @Override
    public BookReview edit(BookReview bookReview) {
        validateBookReview(bookReview);
        prepareBook(bookReview);
        BookReviewId bookReviewId = bookReviewRepository.update(bookReview);
        if (bookReviewId != null) {
            return bookReviewRepository.get(bookReviewId);
        }
        return null;
    }

    @Override
    public BookReviewId remove(BookReviewId id) {
        return bookReviewRepository.delete(id);
    }

    private void prepareBook(BookReview bookReview) {
        bookReview.setBook(bookRepository.get(bookReview.getBook().getBookId()));
    }

    private void validateBookReview(BookReview bookReview) {
        if (bookReview.getBook() == null || bookReview.getBook().getBookId() == null) {
            throw new IllegalArgumentException("Book is not set");
        }
        if (bookReview.getRating() < 1 || bookReview.getRating() > 10) {
            throw new IllegalArgumentException("Review rating must be between 1 and 10");
        }
    }
}