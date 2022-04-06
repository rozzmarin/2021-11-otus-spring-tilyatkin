package ru.otus.spring.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.dao.exception.ObjectNotFoundException;
import ru.otus.spring.domain.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDaoJdbcTest {
    private static final Book book1 = new Book(
            new BookId(1),
            "Евгений Онегин",
            Set.of(new Author(new AuthorId(1), "Пушкин", "Александр Сергеевич")),
            Set.of(new Genre(new GenreId(1), "Роман")));
    private static final Book book2 = new Book(
            new BookId(2),
            "Война и мир",
            Set.of(new Author(new AuthorId(2), "Толстой", "Лев Николаевич")),
            Set.of(new Genre(new GenreId(1), "Роман"), new Genre(new GenreId(3), "Эпопея")));
    private static final Book book2ToEdit = new Book(
            new BookId(2),
            "Война и мир",
            Set.of(new Author(new AuthorId(2)), new Author("Толстой", "Алексей Константинович")),
            Set.of(new Genre(new GenreId(1))));
    private static final Book book2AfterEdit = new Book(
            new BookId(2),
            "Война и мир",
            Set.of(new Author(new AuthorId(2), "Толстой", "Лев Николаевич"), new Author(new AuthorId(5), "Толстой", "Алексей Константинович")),
            Set.of(new Genre(new GenreId(1), "Роман")));
    private static final Book book3ToAdd = new Book(
            "Герой нашего времени",
            Set.of(new Author("Лермонтов", "Михаил Юрьевич")),
            Set.of(new Genre(new GenreId(2))));
    private static final Book book3AfterAdd = new Book(
            new BookId(3),
            "Герой нашего времени",
            Set.of(new Author(new AuthorId(4), "Лермонтов", "Михаил Юрьевич")),
            Set.of(new Genre(new GenreId(2), "Поэма")));

    @Autowired
    private BookDaoJdbc bookDao;

    @ComponentScan("ru.otus.spring.dao")
    @Configuration
    static class NestedConfiguration {
    }

    @Test
    void shouldReturnAllBooksCount() {
        long expectedBooksCount = bookDao.count(BookFilter.builder().build());
        assertThat(expectedBooksCount)
                .isEqualTo(2);
    }

    @Test
    void shouldReturnFilteredBooksCount() {
        long expectedBooksCount = bookDao.count(BookFilter.builder()
                .title("Война")
                .build());
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookDao.count(BookFilter.builder()
                .authorFilter(AuthorFilter.builder()
                        .name("Александр")
                        .build())
                .genreFilter(GenreFilter.builder()
                        .title("Роман")
                        .build())
                .build());
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookDao.count(BookFilter.builder()
                .title("Война")
                .authorFilter(AuthorFilter.builder()
                        .name("Александр")
                        .build())
                .genreFilter(GenreFilter.builder()
                        .title("Роман")
                        .build())
                .build());
        assertThat(expectedBooksCount)
                .isEqualTo(0);
    }

    @Test
    void shouldReturnBook() {
        Book actualBook = bookDao.get(new BookId(1));
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(book1);
    }

    @Test
    void shouldReturnAllBooks() {
        List<Book> actualBooks = bookDao.get(BookFilter.builder().build());
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(book1, book2));
    }

    @Test
    void shouldReturnFilteredBooks() {
        List<Book> actualBooks = bookDao.get(BookFilter.builder()
                .authorFilter(AuthorFilter.builder()
                        .name("Александр")
                        .build())
                .genreFilter(GenreFilter.builder()
                        .title("Роман")
                        .build())
                .build());
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(book1));
    }

    @Test
    @Order(1)
    void shouldInsertBook() {
        BookId actualBookId = bookDao.insert(book3ToAdd);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(book3AfterAdd.getBookId());
        Book actualBook = bookDao.get(actualBookId);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book3AfterAdd);
    }

    @Test
    @Order(2)
    void shouldUpdateBook() {
        BookId actualBookId = bookDao.update(book2ToEdit);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(book2AfterEdit.getBookId());
        Book actualBook = bookDao.get(actualBookId);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book2AfterEdit);
    }

    @Test
    void shouldDeleteBook() {
        BookId bookId = new BookId(2);
        Book actualBook = bookDao.get(bookId);
        assertThat(actualBook)
                .isNotNull();
        BookId actualBookId = bookDao.delete(bookId);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(bookId);
        assertThatThrownBy(() -> bookDao.get(bookId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}