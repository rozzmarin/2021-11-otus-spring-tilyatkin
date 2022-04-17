package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import ru.otus.spring.domain.BookId;
import ru.otus.spring.dto.BookReviewCount;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class BookReviewRepositoryImpl implements BookReviewRepositoryCustom
{
    private final MongoOperations mongoOperations;

    public Map<BookId, Long> countAtBooks(Set<BookId> bookIds) {
        Aggregation countAggregation = newAggregation(
                match(where("book._id").in(bookIds)),
                group("book._id").count().as("reviewsCount"),
                addFields().addFieldWithValue("bookId", "$_id").build());
        List<BookReviewCount> bookReviewCountList = mongoOperations.aggregate(countAggregation, "bookReviews", BookReviewCount.class)
                .getMappedResults();
        return bookReviewCountList
                .stream()
                .collect(Collectors.toMap(BookReviewCount::getBookId, BookReviewCount::getReviewsCount));
    }
}