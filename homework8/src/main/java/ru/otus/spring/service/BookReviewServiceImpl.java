package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.BookReviewSpecification;
import ru.otus.spring.service.idGenerator.IdentifierGenerator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookReviewServiceImpl implements BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;
    private final BookReviewSpecification bookReviewSpecification;
    private final IdentifierGenerator<BookReviewId> bookReviewIdGenerator;

    @Override
    @Transactional(readOnly = true)
    public BookReview find(BookReviewId id) {
        BookReview bookReview = bookReviewRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("BookReview with id %s is not found", id)));
        bookReview.setBook(bookRepository.findTitleAndAuthorsByBookId(bookReview.getBook().getBookId()).orElseThrow());
        return bookReview;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookReview> find(BookReviewFilter filter) {
        List<BookReview> bookReviews = bookReviewRepository.findAll(bookReviewSpecification.toPredicate(filter));
        Map<BookId, Book> bookMap = bookRepository
                .findTitleAndAuthorsByBookIdIn(bookReviews.stream()
                        .map(bookReview -> bookReview.getBook().getBookId())
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Book::getBookId, book -> book));
        bookReviews.forEach(bookReview -> bookReview.setBook(bookMap.get(bookReview.getBook().getBookId())));
        return bookReviews;
    }

    @Override
    public BookReview add(BookReview bookReview) {
        validateBookReview(bookReview);
        bookReview.setBookReviewId(bookReviewIdGenerator.generate());
        return bookReviewRepository.save(bookReview);
    }

    @Override
    public BookReview edit(BookReview bookReview) {
        validateBookReview(bookReview);
        return bookReviewRepository.save(bookReview);
    }

    @Override
    public BookReviewId remove(BookReviewId id) {
        bookReviewRepository.deleteById(id);
        return id;
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