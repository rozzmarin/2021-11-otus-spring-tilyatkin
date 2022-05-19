package ru.otus.spring.repository.specification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.domain.BookReview;
import ru.otus.spring.domain.BookReviewFilter;
import ru.otus.spring.domain.RatingLevel;
import ru.otus.spring.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class BookReviewSpecification implements BaseSpecification<BookReview> {
    private final BookReviewFilter filter;

    @Override
    public Predicate toPredicate(Path<BookReview> path, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.isBookIdsSpecified()) {
            predicates.add(path.get("book").get("bookId").in(filter.getBookIds()));
        }
        if (filter.isReviewerNameSpecified()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path.get("reviewerName")), StringUtils.quoted(filter.getReviewerName().toLowerCase(), '%')));
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
            predicates.add(path.get("rating").in(ratings));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public static BookReviewSpecification of(BookReviewFilter filter) {
        return new BookReviewSpecification(filter);
    }
}