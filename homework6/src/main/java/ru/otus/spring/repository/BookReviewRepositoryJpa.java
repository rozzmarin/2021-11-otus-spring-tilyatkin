package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.*;
import ru.otus.spring.util.StringUtils;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookReviewRepositoryJpa implements BookReviewRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count(BookReviewFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect count(br)")
                .append("\nfrom BookReview br")
                .append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        Query query = entityManager.createQuery(sql.toString());
        parametersMap.forEach(query::setParameter);
        return (long)query.getSingleResult();
    }

    @Override
    public Map<BookId, Long> countAtBooks(Set<BookId> bookIds) {
        String bookIdsString = bookIds
                .stream()
                .map(id ->  Long.toString(id.getBookId()))
                .collect(Collectors.joining(","));
        StringBuilder sql = new StringBuilder()
                .append("\nselect br.book.bookId as bookId, count(br) as count")
                .append("\nfrom BookReview br")
                .append("\nwhere br.book.bookId in")
                .append("\n  (")
                .append(bookIdsString)
                .append("\n  )")
                .append("\ngroup by br.book.bookId");
        TypedQuery<Tuple> query = entityManager.createQuery(sql.toString(), Tuple.class);
        return query.getResultList().stream()
            .collect(Collectors.toMap(tuple -> tuple.get("bookId", BookId.class), tuple -> tuple.get("count", Long.class)));
    }

    @Override
    public BookReview get(BookReviewId id) {
        BookReview bookReview = entityManager.find(
                BookReview.class,
                id,
                Map.of("javax.persistence.fetchgraph", entityManager.getEntityGraph("book-review-entity-graph-with-book-authors")));
        if (bookReview == null) {
            throw new ObjectNotFoundException(String.format("BookReview with id %s is not found", id.getBookReviewId()));
        }
        return bookReview;
    }

    @Override
    public List<BookReview> get(BookReviewFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect br")
                .append("\nfrom BookReview br")
                .append("\n  join fetch br.book")
                .append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        TypedQuery<BookReview> query = entityManager
                .createQuery(sql.toString(), BookReview.class)
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("book-review-entity-graph-with-book-authors"));
        parametersMap.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public BookReviewId insert(BookReview bookReview) {
        entityManager.persist(bookReview);
        entityManager.flush();
        return bookReview.getBookReviewId();
    }

    @Override
    public BookReviewId update(BookReview bookReview) {
        bookReview = entityManager.merge(bookReview);
        return bookReview.getBookReviewId();
    }

    @Override
    public BookReviewId delete(BookReviewId id) {
        BookReview bookReview = entityManager.find(BookReview.class, id);
        if (bookReview != null) {
            entityManager.remove(bookReview);
            entityManager.flush();
            return bookReview.getBookReviewId();
        }
        return null;
    }

    private void createFilter(StringBuilder sql, Map<String, Object> parametersMap, BookReviewFilter filter) {
        if (filter.isBookIdsSpecified()) {
            String bookIdsString = filter.getBookIds()
                    .stream()
                    .map(id ->  Long.toString(id.getBookId()))
                    .collect(Collectors.joining(","));
            sql.append("\n  and br.book.bookId in");
            sql.append("\n  (");
            sql.append(bookIdsString);
            sql.append("\n  )");
        }
        if (filter.isReviewerNameSpecified()) {
            sql.append("\n  and br.reviewerName like :reviewerName");
            parametersMap.put("reviewerName", StringUtils.quoted(filter.getReviewerName(), '%'));
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
            String ratingsString = ratings
                    .stream()
                    .map(rating -> Integer.toString(rating))
                    .collect(Collectors.joining(","));
            sql.append("\n  and br.rating in");
            sql.append("\n  (");
            sql.append(ratingsString);
            sql.append("\n  )");
        }
    }
}