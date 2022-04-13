package ru.otus.spring.repository;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(BookReviewRepositoryJpa.class)
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
            .bookReviewId(new BookReviewId(1))
            .book(new Book(new BookId(2)))
            .reviewerName("Вася Пупкин")
            .rating(9)
            .comment("Дочитал. Эпично")
            .build();
    private static final BookReview bookReview2AfterEdit = BookReview.builder()
            .bookReviewId(new BookReviewId(1))
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
    private BookReviewRepositoryJpa bookReviewRepository;

    @Test
    void shouldReturnAllBookReviewsCount() {
        long expectedBookReviewsCount = bookReviewRepository.count(BookReviewFilter.builder().build());
        assertThat(expectedBookReviewsCount)
                .isEqualTo(2);
    }

    @Test
    void shouldReturnFilteredBookReviewsCount() {
        long expectedBooksCount = bookReviewRepository.count(BookReviewFilter.builder()
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build());
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookReviewRepository.count(BookReviewFilter.builder()
                .reviewerName("Дантес")
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build());
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookReviewRepository.count(BookReviewFilter.builder()
                .bookIds(Set.of(new BookId(2)))
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build());
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
        BookReview actualBookReview = bookReviewRepository.get(new BookReviewId(1));
        assertThat(actualBookReview)
                .usingRecursiveComparison(comparisonConfiguration)
                .isEqualTo(bookReview1);
    }

    @Test
    void shouldReturnAllBookReviews() {
        List<BookReview> actualBookReviews = bookReviewRepository.get(BookReviewFilter.builder().build());
        assertThat(actualBookReviews)
                .usingRecursiveFieldByFieldElementComparator(comparisonConfiguration)
                .containsExactlyInAnyOrderElementsOf(List.of(bookReview1, bookReview2));
    }

    @Test
    void shouldReturnFilteredBookReviews() {
        List<BookReview> actualBookReviews = bookReviewRepository.get(BookReviewFilter.builder()
                .reviewerName("Дантес")
                .ratingLevel(Set.of(RatingLevel.LOW))
                .build());
        assertThat(actualBookReviews)
                .usingRecursiveFieldByFieldElementComparator(comparisonConfiguration)
                .containsExactlyInAnyOrderElementsOf(List.of(bookReview1));
    }

    @Test
    void shouldInsertBookReview() {
        BookReviewId actualBookReviewId = bookReviewRepository.insert(bookReview3ToAdd);
        assertThat(actualBookReviewId)
                .isNotNull()
                .isEqualTo(bookReview3AfterAdd.getBookReviewId());
        BookReview actualBookReview = bookReviewRepository.get(actualBookReviewId);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison(comparisonConfiguration)
                .isEqualTo(bookReview3AfterAdd);
    }

    @Test
    void shouldUpdateBookReview() {
        BookReviewId actualBookReviewId = bookReviewRepository.update(bookReview2ToEdit);
        assertThat(actualBookReviewId)
                .isNotNull()
                .isEqualTo(bookReview2AfterEdit.getBookReviewId());
        BookReview actualBookReview = bookReviewRepository.get(actualBookReviewId);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison(comparisonConfiguration)
                .isEqualTo(bookReview2ToEdit);
    }

    @Test
    void shouldDeleteBookReview() {
        BookReviewId bookReviewId = new BookReviewId(2);
        BookReview actualBookReview = bookReviewRepository.get(bookReviewId);
        assertThat(actualBookReview)
                .isNotNull();
        BookReviewId actualBookReviewId = bookReviewRepository.delete(bookReviewId);
        assertThat(actualBookReviewId)
                .isNotNull()
                .isEqualTo(bookReviewId);
        assertThatThrownBy(() -> bookReviewRepository.get(bookReviewId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}