package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.BookReviewSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookReviewServiceImpl implements BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;


    @Override
    @Transactional(readOnly = true)
    public BookReview find(BookReviewId id) {
        return bookReviewRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("BookReview with id %s is not found", id.getBookReviewId())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookReview> find(BookReviewFilter filter) {
        return bookReviewRepository.findAll(BookReviewSpecification.of(filter));
    }

    @Override
    public BookReview add(BookReview bookReview) {
        validateBookReview(bookReview);
        prepareBook(bookReview);
        return bookReviewRepository.save(bookReview);
    }

    @Override
    public BookReview edit(BookReview bookReview) {
        validateBookReview(bookReview);
        prepareBook(bookReview);
        return bookReviewRepository.save(bookReview);
    }

    @Override
    public BookReviewId remove(BookReviewId id) {
        bookReviewRepository.deleteById(id);
        return id;
    }

    private void prepareBook(BookReview bookReview) {
        bookReview.setBook(bookRepository.findById(bookReview.getBook().getBookId()).orElseThrow());
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