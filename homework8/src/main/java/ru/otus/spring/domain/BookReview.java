package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "bookReviews")
public class BookReview {
    @Id
    private BookReviewId bookReviewId;
    private Book book;
    private String reviewerName;
    private int rating;
    private String comment;

    public BookReview(BookReviewId bookReviewId) {
        this.bookReviewId = bookReviewId;
    }
}
