package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;
import ru.otus.spring.repository.specification.BookReviewSpecification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BookReviewServiceImplTest {
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
    private static final Book book2 = Book.builder()
            .bookId(new BookId(2))
            .title("Война и мир")
            .authors(Set.of(author2))
            .genres(Set.of(genre1, genre3))
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
    private static final BookReview bookReview2ToEditPrepared = BookReview.builder()
            .bookReviewId(new BookReviewId(1))
            .book(book2)
            .reviewerName("Вася Пупкин")
            .rating(9)
            .comment("Дочитал. Эпично")
            .build();
    private static final BookReview bookReview2AfterEdit = BookReview.builder()
            .bookReviewId(new BookReviewId(1))
            .book(book2)
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
    private static final BookReview bookReview3ToAddPrepared = BookReview.builder()
            .book(book2)
            .reviewerName("Иван Иванович")
            .rating(10)
            .comment("Настольная книга")
            .build();
    private static final BookReview bookReview3AfterAdd = BookReview.builder()
            .bookReviewId(new BookReviewId(3))
            .book(book2)
            .reviewerName("Иван Иванович")
            .rating(10)
            .comment("Настольная книга")
            .build();

    @MockBean
    private BookReviewRepository bookReviewRepository;
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private BookReviewServiceImpl bookReviewService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        BookReviewServiceImpl bookReviewService(BookReviewRepository bookReviewRepository, BookRepository bookRepository) {
            return new BookReviewServiceImpl(bookReviewRepository, bookRepository);
        }
    }

    @Test
    void shouldReturnBookReview() {
        BookReviewId bookReviewId = new BookReviewId(1);
        given(bookReviewRepository.findById(bookReviewId))
                .willReturn(Optional.of(bookReview1));

        BookReview actualBookReview = bookReviewService.find(bookReviewId);
        assertThat(actualBookReview)
                .usingRecursiveComparison()
                .isEqualTo(bookReview1);
    }

    @Test
    void shouldReturnBookReviews() {
        BookReviewFilter filter = BookReviewFilter.builder().build();
        given(bookReviewRepository.findAll(BookReviewSpecification.of(filter)))
                .willReturn(List.of(bookReview1, bookReview2));

        List<BookReview> actualBookReviews = bookReviewService.find(filter);
        assertThat(actualBookReviews)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(bookReview1, bookReview2));
    }

    @Test
    void shouldInsertBookReview() {
        given(bookRepository.findById(new BookId(2)))
                .willReturn(Optional.of(book2));
        given(bookReviewRepository.save(bookReview3ToAddPrepared))
                .willReturn(bookReview3AfterAdd);

        BookReview actualBookReview = bookReviewService.add(bookReview3ToAdd);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookReview3AfterAdd);
    }

    @Test
    void shouldUpdateBookReview() {
        given(bookRepository.findById(new BookId(2)))
                .willReturn(Optional.of(book2));
        given(bookReviewRepository.save(bookReview2ToEditPrepared))
                .willReturn(bookReview2AfterEdit);

        BookReview actualBookReview = bookReviewService.edit(bookReview2ToEdit);
        assertThat(actualBookReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookReview2AfterEdit);
    }

    @Test
    void shouldDeleteBookReview() {
        BookReviewId bookReviewId = new BookReviewId(1);

        BookReviewId actualBookReviewId = bookReviewService.remove(bookReviewId);
        assertThat(actualBookReviewId)
                .isNotNull()
                .isEqualTo(bookReviewId);
    }
}