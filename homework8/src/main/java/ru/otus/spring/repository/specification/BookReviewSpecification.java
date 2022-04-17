package ru.otus.spring.repository.specification;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.*;
import ru.otus.spring.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookReviewSpecification implements BaseSpecification<BookReviewFilter> {
    @Override
    public Predicate toPredicate(BookReviewFilter filter) {
        BooleanBuilder expressionBuilder = new BooleanBuilder();
        if (filter.isBookIdsSpecified()) {
            expressionBuilder.and(QBookReview.bookReview.book.bookId.in(filter.getBookIds()));
        }
        if (filter.isReviewerNameSpecified()) {
            expressionBuilder.and(QBookReview.bookReview.reviewerName.like(StringUtils.quoted(filter.getReviewerName(), '%')));
        }
        if (filter.isRatingLevelSpecified()) {
            List<Integer> ratings = new ArrayList<>();
            if (filter.getRatingLevel().contains(RatingLevel.LOW)) {
                ratings.addAll(List.of(1, 2, 3));
            }
            if (filter.getRatingLevel().contains(RatingLevel.MIDDLE)) {
                ratings.addAll(List.of(4, 5, 6, 7));
            }
            if (filter.getRatingLevel().contains(RatingLevel.HIGH)) {
                ratings.addAll(List.of(8, 9, 10));
            }
            expressionBuilder.and(QBookReview.bookReview.rating.in(ratings));
        }
        return expressionBuilder;
    }
}