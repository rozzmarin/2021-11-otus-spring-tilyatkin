package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.*;
import ru.otus.spring.domain.exception.InvalidOperationException;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;
import ru.otus.spring.domain.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.BookReviewSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public BookReview find(BookReviewId id) {
        return bookReviewRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Отзыв с кодом %s не найден", id.getBookReviewId())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookReview> find(BookReviewFilter filter) {
        return bookReviewRepository.findAll(BookReviewSpecification.of(filter));
    }

    @Override
    @Transactional
    public BookReview add(BookReview bookReview) {
        validateBookReview(bookReview, Operation.ADD);
        prepareBook(bookReview);
        return bookReviewRepository.save(bookReview);
    }

    @Override
    @Transactional
    public BookReview edit(BookReview bookReview) {
        validateBookReview(bookReview, Operation.EDIT);
        prepareBook(bookReview);
        return bookReviewRepository.save(bookReview);
    }

    @Override
    @Transactional
    public BookReviewId remove(BookReviewId id) {
        validateBookReview(new BookReview(id), Operation.REMOVE);
        bookReviewRepository.deleteById(id);
        return id;
    }

    private void prepareBook(BookReview bookReview) {
        bookReview.setBook(bookRepository.findById(bookReview.getBook().getBookId()).orElseThrow());
    }

    private void validateBookReview(BookReview bookReview, Operation operation) {
        if (operation.isAdd() && bookReview.getBookReviewId() != null) {
            throw new InvalidOperationException("Недопустимый идентификатор");
        }
        if (!operation.isAdd()) {
            if (bookReview.getBookReviewId() == null) {
                throw new InvalidOperationException("Не задан идентификатор");
            }
            if (bookReviewRepository.findById(bookReview.getBookReviewId()).isEmpty()) {
                throw new ObjectNotFoundException(String.format("Отзыв с кодом %s не найден", bookReview.getBookReviewId().getBookReviewId()));
            }
        }
        if (!operation.isRemove()) {
            if (bookReview.getBook() == null || bookReview.getBook().getBookId() == null) {
                throw new IllegalArgumentException("Книга не задана");
            }
            if (bookReview.getRating() < 1 || bookReview.getRating() > 10) {
                throw new IllegalArgumentException("Оценка должна быть от 1 до 10");
            }
        }
    }

    private enum Operation {
        ADD,
        EDIT,
        REMOVE;

        public boolean isAdd() {
            return this == Operation.ADD;
        }

        public boolean isEdit() {
            return this == Operation.EDIT;
        }

        public boolean isRemove() {
            return this == Operation.REMOVE;
        }
    }
}