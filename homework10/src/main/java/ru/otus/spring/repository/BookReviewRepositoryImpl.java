package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookReviewRepositoryImpl implements BookReviewRepositoryCustom
{
    @PersistenceContext
    private final EntityManager entityManager;

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
}