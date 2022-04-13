package ru.otus.spring.repository.specification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookFilter;
import ru.otus.spring.domain.BookReview;
import ru.otus.spring.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class BookSpecification implements BaseSpecification<Book> {
    private final BookFilter filter;

    @Override
    public Predicate toPredicate(Path<Book> path, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.isBookIdsSpecified()) {
            predicates.add(path.get("bookId").in(filter.getBookIds()));
        }
        if (filter.isTitleSpecified()) {
            predicates.add(criteriaBuilder.like(path.get("title"), StringUtils.quoted(filter.getTitle(), '%')));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(toPredicate(root, criteriaBuilder));
        if (filter.isReviewsCountSpecified()) {
            Subquery<Long> bookReviewsCountQuery = query.subquery(Long.class);
            Root<BookReview> bookReviewRoot = bookReviewsCountQuery.from(BookReview.class);
            bookReviewsCountQuery
                    .where(criteriaBuilder.equal(bookReviewRoot.get("book").get("bookId"), root.get("bookId")))
                    .select(criteriaBuilder.count(bookReviewRoot));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(bookReviewsCountQuery, filter.getReviewsCount()));
        }
        if (filter.isAuthorFilterSpecified()) {
            query.distinct(true);
            AuthorSpecification authorSpecification = AuthorSpecification.of(filter.getAuthorFilter());
            predicates.add(authorSpecification.toPredicate(root.join("authors"), criteriaBuilder));
        }
        if (filter.isGenreFilterSpecified()) {
            query.distinct(true);
            GenreSpecification genreSpecification = GenreSpecification.of(filter.getGenreFilter());
            predicates.add(genreSpecification.toPredicate(root.join("genres"), criteriaBuilder));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public static BookSpecification of(BookFilter filter) {
        return new BookSpecification(filter);
    }
}