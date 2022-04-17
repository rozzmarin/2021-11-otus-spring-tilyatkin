package ru.otus.spring.repository;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.specification.BookReviewSpecification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookReviewRepositoryJpaTest {
    private static final Author author2 = Author.builder()
            .authorId(new AuthorId(2))
            .lastname("Толстой")
            .firstname("Лев Николаевич")
            .build();
    private static final Genre genre1 = Genre.builder()
            .genreId(new GenreId(1))
            .title("Роман")
            .build();
    private static final Genre genre3 = Genre.builder()
            .genreId(new GenreId(3))
            .title("Эпопея")
            .build();
    private static final BookReview bookReview1 = BookReview.builder()
            .bookReviewId(new BookReviewId(1))
            .book(new Book(new BookId(1)))
            .reviewerName("Дантес")
            .rating(2)
            .comment("Так себе")
            .build();
    private static final BookReview bookReview2 = BookReview.builder()
            .bookReviewId(new BookReviewId(2))
            .book(new Book(new BookId(2)))
            .reviewerName("Вася Пупкин")
            .rating(5)
            .comment("Не осилил")
            .build();
    private static final BookReview bookReview2ToEdit = BookReview.builder()
            .bookReviewId(new BookReviewId(2))
            .book(new Book(new BookId(2)))
            .reviewerName("Вася Пупкин")
            .rating(9)
            .comment("Дочитал. Эпично")
            .build();
    private static final BookReview bookReview2AfterEdit = BookReview.builder()
            .bookReviewId(new BookReviewId(2))
            .book(new Book(new BookId(2)))
            .reviewerName("Вася Пупкин")
            .rating(9)
            .comment("Дочитал. Эпично")
            .build();
    private static final BookReview bookReview3ToAdd = BookReview.builder()
            .book(new Book(new BookId(2)))
            .reviewerName("Иван Иванович")
            .rating(10)
            .comment("Настольная книга")
            .build();
    private static final BookReview bookReview3AfterAdd = BookReview.builder()
            .bookReviewId(new BookReviewId(3))
            .book(new Book(new BookId(2)))
            .reviewerName("Иван Иванович")
            .rating(10)
            .comment("Настольная книга")
            .build();
    private static final RecursiveComparisonConfiguration comparisonConfiguration = RecursiveComparisonConfiguration.builder()
            .withComparatorForType(Comparator.comparing(Book::getBookId), Book.class)
            .build();

    @Autowired
    private BookReviewRepository bookReviewRepository;

    @Test
    void shouldReturnAllBookReviewsCount() {
        long expectedBookReviewsCount = bookReviewRepository.count(BookReviewSpecification.of(BookReviewFilter.builder().build()));
        assertThat(expectedBookReviewsCount)
                .isEqualTo(2);
    }

    @Test
    void shouldReturnFilteredBookReviewsCount() {
        long expectedBooksCount = bookReviewRepository.count(BookReviewSpecification.of(BookReviewFilter.builder()
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookReviewRepository.count(BookReviewSpecification.of(BookReviewFilter.builder()
                .reviewerName("Дантес")
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookReviewRepository.count(BookReviewSpecification.of(BookReviewFilter.builder()
                .bookIds(Set.of(new BookId(2)))
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(0);
    }

    @Test
    void shouldReturnBookReviewsCountAtBooks() {
        Map<BookId, Long> expectedBookReviewsCountMap = bookReviewRepository.countAtBooks(Set.of(new BookId(1), new BookId(2)));
        assertThat(expectedBookReviewsCountMap)
                .isNotNull()
                .containsExactlyInAnyOrderEntriesOf(Map.of(new BookId(1), 1L, new BookId(2), 1L));
    }

    @Test
    void shouldReturnBookReview() {
        Optional<BookReview> actualBookReview = bookReviewRepository.findById(new BookReviewId(1));
        assertThat(actualBookReview)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison(comparisonConfiguration)
                .isEqualTo(bookReview1);
    }

    @Test
    void shouldReturnAllBookReviews() {
        List<BookReview> actualBookReviews = bookReviewRepository.findAll(BookReviewSpecification.of(BookReviewFilter.builder().build()));
        assertThat(actualBookReviews)
                .usingRecursiveFieldByFieldElementComparator(comparisonConfiguration)
                .containsExactlyInAnyOrderElementsOf(List.of(bookReview1, bookReview2));
    }

    @Test
    void shouldReturnFilteredBookReviews() {
        List<BookReview> actualBookReviews = bookReviewRepository.findAll(BookReviewSpecification.of(BookReviewFilter.builder()
                .reviewerName("Дантес")
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build()));
        assertThat(actualBookReviews)
                .usingRecursiveFieldByFieldElementComparator(comparisonConfiguration)
                .containsExactlyInAnyOrderElementsOf(List.of(bookReview1));
    }

    @Test
    void shouldInsertBookReview() {
        BookReview actualBookReview = bookReviewRepository.saveAndFlush(bookReview3ToAdd);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison(comparisonConfiguration)
                .isEqualTo(bookReview3AfterAdd);
    }

    @Test
    void shouldUpdateBookReview() {
        BookReview actualBookReview = bookReviewRepository.saveAndFlush(bookReview2ToEdit);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison(comparisonConfiguration)
                .isEqualTo(bookReview2AfterEdit);
    }

    @Test
    void shouldDeleteBookReview() {
        BookReviewId bookReviewId = new BookReviewId(2);
        assertThat(bookReviewRepository.findById(bookReviewId))
                .isNotEmpty();
        bookReviewRepository.deleteById(bookReviewId);
        bookReviewRepository.flush();
        assertThat(bookReviewRepository.findById(bookReviewId))
                .isEmpty();
    }
}