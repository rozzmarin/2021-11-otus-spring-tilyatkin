package ru.otus.spring.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.specification.BookSpecification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(BookSpecification.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryTest {
    private static final Author author1 = Author.builder()
            .authorId(new AuthorId("A001"))
            .lastname("Пушкин")
            .firstname("Александр Сергеевич")
            .build();
    private static final Author author2 = Author.builder()
            .authorId(new AuthorId("A002"))
            .lastname("Толстой")
            .firstname("Лев Николаевич")
            .build();
    private static final Author author3 = Author.builder()
            .authorId(new AuthorId("A003"))
            .lastname("Тургенев")
            .firstname("Иван Сергеевич")
            .build();
    private static final Genre genre1 = Genre.builder()
            .genreId(new GenreId("G001"))
            .title("Роман")
            .build();
    private static final Genre genre2 = Genre.builder()
            .genreId(new GenreId("G002"))
            .title("Поэма")
            .build();
    private static final Genre genre3 = Genre.builder()
            .genreId(new GenreId("G003"))
            .title("Эпопея")
            .build();
    private static final Book book1 = Book.builder()
            .bookId(new BookId("B001"))
            .title("Евгений Онегин")
            .authors(Set.of(author1))
            .genres(Set.of(genre1))
            .build();
    private static final Book book2 = Book.builder()
            .bookId(new BookId("B002"))
            .title("Война и мир")
            .authors(Set.of(author2))
            .genres(Set.of(genre1, genre3))
            .build();
    private static final Book book2ToEdit = Book.builder()
            .bookId(new BookId("B003"))
            .title("Война и мир")
            .authors(Set.of(author2, author3))
            .genres(Set.of(genre1))
            .build();
    private static final Book book2AfterEdit = book2ToEdit;
    private static final Book book3ToAdd = Book.builder()
            .bookId(new BookId("B003"))
            .title("Отцы и дети")
            .authors(Set.of(author3))
            .genres(Set.of(genre1))
            .build();
    private static final Book book3AfterAdd = book3ToAdd;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookSpecification bookSpecification;

    @Test
    @Order(1)
    void shouldReturnAllBooksCount() {
        long expectedBooksCount = bookRepository.count(bookSpecification.toPredicate(BookFilter.builder().build()));
        assertThat(expectedBooksCount)
                .isEqualTo(2);
    }

    @Test
    @Order(1)
    void shouldReturnFilteredBooksCount() {
        long expectedBooksCount = bookRepository.count(bookSpecification.toPredicate(BookFilter.builder()
                .title("Война")
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookRepository.count(bookSpecification.toPredicate(BookFilter.builder()
                .authorFilter(AuthorFilter.builder()
                        .name("Александр")
                        .build())
                .genreFilter(GenreFilter.builder()
                        .title("Роман")
                        .build())
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookRepository.count(bookSpecification.toPredicate(BookFilter.builder()
                .title("Война")
                .authorFilter(AuthorFilter.builder()
                        .name("Александр")
                        .build())
                .genreFilter(GenreFilter.builder()
                        .title("Роман")
                        .build())
                .build()));
        assertThat(expectedBooksCount)
                .isEqualTo(0);
    }

    @Test
    @Order(1)
    void shouldReturnBook() {
        Optional<Book> actualBook = bookRepository.findById(new BookId("B001"));
        assertThat(actualBook)
                .isNotEmpty()
                .hasValue(book1);
    }

    @Test
    @Order(1)
    void shouldReturnAllBooks() {
        List<Book> actualBooks = bookRepository.findAll(bookSpecification.toPredicate(BookFilter.builder().build()));
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(book1, book2));
    }

    @Test
    @Order(1)
    void shouldReturnFilteredBooks() {
        List<Book> actualBooks = bookRepository.findAll(bookSpecification.toPredicate(BookFilter.builder()
                .authorFilter(AuthorFilter.builder()
                        .name("Александр")
                        .build())
                .genreFilter(GenreFilter.builder()
                        .title("Роман")
                        .build())
                .build()));
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(book1));
    }

    @Test
    @Order(2)
    void shouldInsertBook() {
        Book actualBook = bookRepository.save(book3ToAdd);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book3AfterAdd);
    }

    @Test
    @Order(3)
    void shouldUpdateBook() {
        Book actualBook = bookRepository.save(book2ToEdit);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book2AfterEdit);
    }

    @Test
    @Order(4)
    void shouldDeleteBook() {
        BookId bookId = new BookId("B002");
        assertThat(bookRepository.findById(bookId))
                .isNotEmpty();
        bookRepository.deleteById(bookId);
        assertThat(bookRepository.findById(bookId))
                .isEmpty();
    }
}