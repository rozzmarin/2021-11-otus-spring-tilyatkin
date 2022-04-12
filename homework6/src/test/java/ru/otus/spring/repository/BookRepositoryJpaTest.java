package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(BookRepositoryJpa.class)
public class BookRepositoryJpaTest {
    private static final Author author1 = Author.builder()
            .authorId(new AuthorId(1))
            .lastname("Пушкин")
            .firstname("Александр Сергеевич")
            .build();
    private static final Author author2 = Author.builder()
            .authorId(new AuthorId(2))
            .lastname("Толстой")
            .firstname("Лев Николаевич")
            .build();
    private static final Author author3 = Author.builder()
            .authorId(new AuthorId(3))
            .lastname("Тургенев")
            .firstname("Иван Сергеевич")
            .build();
    private static final Genre genre1 = Genre.builder()
            .genreId(new GenreId(1))
            .title("Роман")
            .build();
    private static final Genre genre2 = Genre.builder()
            .genreId(new GenreId(2))
            .title("Поэма")
            .build();
    private static final Genre genre3 = Genre.builder()
            .genreId(new GenreId(3))
            .title("Эпопея")
            .build();
    private static final Book book1 = Book.builder()
            .bookId(new BookId(1))
            .title("Евгений Онегин")
            .authors(Set.of(author1))
            .genres(Set.of(genre1))
            .build();
    private static final Book book2 = Book.builder()
            .bookId(new BookId(2))
            .title("Война и мир")
            .authors(Set.of(author2))
            .genres(Set.of(genre1, genre3))
            .build();
    private static final Book book2ToEdit = Book.builder()
            .bookId(new BookId(2))
            .title("Война и мир")
            .authors(Set.of(author2, author3))
            .genres(Set.of(genre1))
            .build();
    private static final Book book2AfterEdit = Book.builder()
            .bookId(new BookId(2))
            .title("Война и мир")
            .authors(Set.of(author2, author3))
            .genres(Set.of(genre1))
            .build();
    private static final Book book3ToAdd = Book.builder()
            .title("Отцы и дети")
            .authors(Set.of(author3))
            .genres(Set.of(genre1))
            .build();
    private static final Book book3AfterAdd = Book.builder()
            .bookId(new BookId(3))
            .title("Отцы и дети")
            .authors(Set.of(author3))
            .genres(Set.of(genre1))
            .build();

    @Autowired
    private BookRepositoryJpa bookRepository;

    @Test
    void shouldReturnAllBooksCount() {
        long expectedBooksCount = bookRepository.count(BookFilter.builder().build());
        assertThat(expectedBooksCount)
                .isEqualTo(2);
    }

    @Test
    void shouldReturnFilteredBooksCount() {
        long expectedBooksCount = bookRepository.count(BookFilter.builder()
                .title("Война")
                .build());
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookRepository.count(BookFilter.builder()
                .authorFilter(AuthorFilter.builder()
                        .name("Александр")
                        .build())
                .genreFilter(GenreFilter.builder()
                        .title("Роман")
                        .build())
                .build());
        assertThat(expectedBooksCount)
                .isEqualTo(1);

        expectedBooksCount = bookRepository.count(BookFilter.builder()
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
        Book actualBook = bookRepository.get(new BookId(1));
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(book1);
    }

    @Test
    void shouldReturnAllBooks() {
        List<Book> actualBooks = bookRepository.get(BookFilter.builder().build());
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(book1, book2));
    }

    @Test
    void shouldReturnFilteredBooks() {
        List<Book> actualBooks = bookRepository.get(BookFilter.builder()
                .reviewsCount(1L)
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
    void shouldInsertBook() {
        BookId actualBookId = bookRepository.insert(book3ToAdd);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(book3AfterAdd.getBookId());
        Book actualBook = bookRepository.get(actualBookId);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book3AfterAdd);
    }

    @Test
    void shouldUpdateBook() {
        BookId actualBookId = bookRepository.update(book2ToEdit);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(book2AfterEdit.getBookId());
        Book actualBook = bookRepository.get(actualBookId);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book2AfterEdit);
    }

    @Test
    void shouldDeleteBook() {
        BookId bookId = new BookId(2);
        Book actualBook = bookRepository.get(bookId);
        assertThat(actualBook)
                .isNotNull();
        BookId actualBookId = bookRepository.delete(bookId);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(bookId);
        assertThatThrownBy(() -> bookRepository.get(bookId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}