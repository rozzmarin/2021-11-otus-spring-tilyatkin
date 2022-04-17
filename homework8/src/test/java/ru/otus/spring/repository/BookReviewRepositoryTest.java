package ru.otus.spring.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.specification.BookReviewSpecification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(BookReviewSpecification.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookReviewRepositoryTest {
    private static final Author author2 = Author.builder()
            .authorId(new AuthorId("A002"))
            .lastname("Толстой")
            .firstname("Лев Николаевич")
            .build();
    private static final Genre genre1 = Genre.builder()
            .genreId(new GenreId("G001"))
            .title("Роман")
            .build();
    private static final Genre genre3 = Genre.builder()
            .genreId(new GenreId("G003"))
            .title("Эпопея")
            .build();
    private static final BookReview bookReview1 = BookReview.builder()
            .bookReviewId(new BookReviewId("BR0001"))
            .book(new Book(new BookId("B001")))
            .reviewerName("Дантес")
            .rating(2)
            .comment("Так себе")
            .build();
    private static final BookReview bookReview2 = BookReview.builder()
            .bookReviewId(new BookReviewId("BR0002"))
            .book(new Book(new BookId("B002")))
            .reviewerName("Вася Пупкин")
            .rating(5)
            .comment("Не осилил")
            .build();
    private static final BookReview bookReview2ToEdit = BookReview.builder()
            .bookReviewId(new BookReviewId("BR0002"))
            .book(new Book(new BookId("B002")))
            .reviewerName("Вася Пупкин")
            .rating(9)
            .comment("Дочитал. Эпично")
            .build();
    private static final BookReview bookReview2AfterEdit = bookReview2ToEdit;
    private static final BookReview bookReview3ToAdd = BookReview.builder()
            .bookReviewId(new BookReviewId("BR0003"))
            .book(new Book(new BookId("B002")))
            .reviewerName("Иван Иванович")
            .rating(10)
            .comment("Настольная книга")
            .build();
    private static final BookReview bookReview3AfterAdd = bookReview3ToAdd;

    @Autowired
    private BookReviewRepository bookReviewRepository;

    @Autowired
    private BookReviewSpecification bookReviewSpecification;

    @Test
    @Order(1)
    void shouldReturnAllBookReviewsCount() {
        long expectedBookReviewsCount = bookReviewRepository.count(bookReviewSpecification.toPredicate(BookReviewFilter.builder().build()));
        assertThat(expectedBookReviewsCount)
                .isEqualTo(2);
    }

    @Test
    @Order(1)
    void shouldReturnFilteredBookReviewsCount() {
        long expectedBooksCount = bookReviewRepository.count(bookReviewSpecification.toPredicate(BookReviewFilter.builder()
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookReviewRepository.count(bookReviewSpecification.toPredicate(BookReviewFilter.builder()
                .reviewerName("Дантес")
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookReviewRepository.count(bookReviewSpecification.toPredicate(BookReviewFilter.builder()
                .bookIds(Set.of(new BookId("B002")))
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(0);
    }

    @Test
    @Order(1)
    void shouldReturnBookReviewsCountAtBooks() {
        Map<BookId, Long> expectedBookReviewsCountMap = bookReviewRepository.countAtBooks(Set.of(new BookId("B001"), new BookId("B002")));
        assertThat(expectedBookReviewsCountMap)
                .isNotNull()
                .containsExactlyInAnyOrderEntriesOf(Map.of(new BookId("B001"), 1L, new BookId("B002"), 1L));
    }

    @Test
    @Order(1)
    void shouldReturnBookReview() {
        Optional<BookReview> actualBookReview = bookReviewRepository.findById(new BookReviewId("BR0001"));
        assertThat(actualBookReview)
                .isNotEmpty()
                .hasValue(bookReview1);
    }

    @Test
    @Order(1)
    void shouldReturnAllBookReviews() {
        List<BookReview> actualBookReviews = bookReviewRepository.findAll(bookReviewSpecification.toPredicate(BookReviewFilter.builder().build()));
        assertThat(actualBookReviews)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(bookReview1, bookReview2));
    }

    @Test
    @Order(1)
    void shouldReturnFilteredBookReviews() {
        List<BookReview> actualBookReviews = bookReviewRepository.findAll(bookReviewSpecification.toPredicate(BookReviewFilter.builder()
                .reviewerName("Дантес")
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(actualBookReviews)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(bookReview1));
    }

    @Test
    @Order(2)
    void shouldInsertBookReview() {
        BookReview actualBookReview = bookReviewRepository.save(bookReview3ToAdd);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookReview3AfterAdd);
    }

    @Test
    @Order(3)
    void shouldUpdateBookReview() {
        BookReview actualBookReview = bookReviewRepository.save(bookReview2ToEdit);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookReview2AfterEdit);
    }

    @Test
    @Order(4)
    void shouldDeleteBookReview() {
        BookReviewId bookReviewId = new BookReviewId("BR0002");
        assertThat(bookReviewRepository.findById(bookReviewId))
                .isNotEmpty();
        bookReviewRepository.deleteById(bookReviewId);
        assertThat(bookReviewRepository.findById(bookReviewId))
                .isEmpty();
    }
}