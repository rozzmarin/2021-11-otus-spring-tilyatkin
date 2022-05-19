package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "BOOK_REVIEW")
@NamedEntityGraph(
        name = "book-review-entity-graph-with-book-authors",
        attributeNodes = @NamedAttributeNode(value = "book", subgraph = "book-authors"),
        subgraphs = @NamedSubgraph(name = "book-authors", attributeNodes = @NamedAttributeNode("authors")))
public class BookReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(type = "ru.otus.spring.repository.hibernate.type.BookReviewIdType", parameters = @Parameter(name = "idColumn", value = "BOOK_REVIEW_ID"))
    @Column(name = "BOOK_REVIEW_ID", nullable = false)
    private BookReviewId bookReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false, updatable = false)
    private Book book;

    @Column(name = "REVIEWER_NAME", nullable = false)
    private String reviewerName;

    @Column(name = "RATING", nullable = false)
    private int rating;

    @Column(name = "COMMENT", nullable = false, length = 4000)
    private String comment;

    public BookReview(BookReviewId bookReviewId) {
        this.bookReviewId = bookReviewId;
    }
}
