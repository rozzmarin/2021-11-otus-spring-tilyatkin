package ru.otus.spring.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.otus.spring.domain.BookId;
import ru.otus.spring.domain.BookReview;
import ru.otus.spring.domain.BookReviewId;

import java.util.List;

public interface BookReviewRepository extends MongoRepository<BookReview, BookReviewId>, QuerydslPredicateExecutor<BookReview>, BookReviewRepositoryCustom {
    List<BookReview> findAll(Predicate predicate);

    void deleteAllByBookBookId(BookId bookId);
}
