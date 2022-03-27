package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BookServiceImplTest {
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

    @MockBean
    private BookDao bookDao;
    @Autowired
    private BookServiceImpl bookService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        BookServiceImpl bookService(BookDao bookDao) {
            return new BookServiceImpl(bookDao);
        }
    }

    @Test
    void shouldReturnBook() {
        BookId bookId = new BookId(1);
        given(bookDao.get(bookId))
                .willReturn(book1);
        Book actualBook = bookService.find(bookId);
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(book1);
    }

    @Test
    void shouldReturnBooks() {
        BookFilter filter = BookFilter.builder().build();
        given(bookDao.get(filter))
                .willReturn(List.of(book1, book2));
        List<Book> actualBooks = bookService.find(filter);
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(book1, book2));
    }

    @Test
    void shouldInsertBook() {
        given(bookDao.insert(book3ToAdd))
                .willReturn(new BookId(3));
        given(bookDao.get(new BookId(3)))
                .willReturn(book3AfterAdd);
        Book actualBook = bookService.add(book3ToAdd);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book3AfterAdd);
    }

    @Test
    void shouldUpdateBook() {
        given(bookDao.update(book2ToEdit))
                .willReturn(new BookId(2));
        given(bookDao.get(new BookId(2)))
                .willReturn(book2AfterEdit);
        Book actualBook = bookService.edit(book2ToEdit);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book2AfterEdit);
    }

    @Test
    void shouldDeleteBook() {
        BookId bookId = new BookId(1);
        given(bookDao.delete(bookId))
                .willReturn(bookId);
        BookId actualBookId = bookService.remove(bookId);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(bookId);
    }
}