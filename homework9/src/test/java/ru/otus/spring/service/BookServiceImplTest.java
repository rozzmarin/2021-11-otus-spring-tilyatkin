package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.repository.specification.BookSpecification;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BookServiceImplTest {
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
    private static final Author author4ToAdd = Author.builder()
            .lastname("Лермонтов")
            .firstname("Михаил Юрьевич")
            .build();
    private static final Author author4AfterAdd = Author.builder()
            .authorId(new AuthorId(4))
            .lastname("Лермонтов")
            .firstname("Михаил Юрьевич")
            .build();
    private static final Author author5ToAdd = Author.builder()
            .lastname("Толстой")
            .firstname("Алексей Константинович")
            .build();
    private static final Author author5AfterAdd = Author.builder()
            .authorId(new AuthorId(5))
            .lastname("Толстой")
            .firstname("Алексей Константинович")
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
    private static final Book book1WithReviewsCount = Book.builder()
            .bookId(new BookId(1))
            .title("Евгений Онегин")
            .authors(Set.of(author1))
            .genres(Set.of(genre1))
            .reviewsCount(1)
            .build();
    private static final Book book2 = Book.builder()
            .bookId(new BookId(2))
            .title("Война и мир")
            .authors(Set.of(author2))
            .genres(Set.of(genre1, genre3))
            .build();
    private static final Book book2WithReviewsCount = Book.builder()
            .bookId(new BookId(2))
            .title("Война и мир")
            .authors(Set.of(author2))
            .genres(Set.of(genre1, genre3))
            .reviewsCount(1)
            .build();
    private static final Book book2ToEdit = Book.builder()
            .bookId(new BookId(2))
            .title("Война и Мир")
            .authors(Set.of(new Author(new AuthorId(2)), author5ToAdd))
            .genres(Set.of(new Genre(new GenreId(1))))
            .build();
    private static final Book book2ToEditPrepared = Book.builder()
            .bookId(new BookId(2))
            .title("Война и Мир")
            .authors(Set.of(author2, author5AfterAdd))
            .genres(Set.of(genre1))
            .build();
    private static final Book book2AfterEdit = Book.builder()
            .bookId(new BookId(2))
            .title("Война и Мир")
            .authors(Set.of(author2, author5AfterAdd))
            .genres(Set.of(genre1))
            .build();
    private static final Book book3ToAdd = Book.builder()
            .title("Герой нашего времени")
            .authors(Set.of(author4ToAdd))
            .genres(Set.of(new Genre(new GenreId(2))))
            .build();
    private static final Book book3ToAddPrepared = Book.builder()
            .title("Герой нашего времени")
            .authors(Set.of(author4AfterAdd))
            .genres(Set.of(genre2))
            .build();
    private static final Book book3AfterAdd = Book.builder()
            .bookId(new BookId(3))
            .title("Герой нашего времени")
            .authors(Set.of(author4AfterAdd))
            .genres(Set.of(genre2))
            .build();

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private BookReviewRepository bookReviewRepository;
    @Autowired
    private BookServiceImpl bookService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        BookServiceImpl bookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, BookReviewRepository bookReviewRepository) {
            return new BookServiceImpl(bookRepository, authorRepository, genreRepository, bookReviewRepository);
        }
    }

    @Test
    void shouldReturnBook() {
        BookId bookId = new BookId(1);
        given(bookRepository.findById(bookId))
                .willReturn(Optional.of(book1));
        given(bookReviewRepository.countAtBooks(Set.of(bookId)))
                .willReturn(Map.of(bookId, 1L));

        Book actualBook = bookService.find(bookId);
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(book1WithReviewsCount);
    }

    @Test
    void shouldReturnBooks() {
        BookFilter filter = BookFilter.builder().build();
        given(bookRepository.findAll(BookSpecification.of(filter)))
                .willReturn(List.of(book1, book2));
        given(bookReviewRepository.countAtBooks(Set.of(book1.getBookId(), book2.getBookId())))
                .willReturn(Map.of(book1.getBookId(), 1L, book2.getBookId(), 1L));

        List<Book> actualBooks = bookService.find(filter);
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(book1WithReviewsCount, book2WithReviewsCount));
    }

    @Test
    void shouldInsertBook() {
        given(authorRepository.save(author4ToAdd))
                .willReturn(author4AfterAdd);
        given(genreRepository.findById(new GenreId(2)))
                .willReturn(Optional.of(genre2));
        given(bookRepository.save(book3ToAddPrepared))
                .willReturn(book3AfterAdd);

        Book actualBook = bookService.add(book3ToAdd);
        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(book3AfterAdd);
    }

    @Test
    void shouldUpdateBook() {
        given(authorRepository.save(author5ToAdd))
                .willReturn(author5AfterAdd);
        given(authorRepository.findById(new AuthorId(2)))
                .willReturn(Optional.of(author2));
        given(genreRepository.findById(new GenreId(1)))
                .willReturn(Optional.of(genre1));
        given(bookRepository.save(book2ToEditPrepared))
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

        BookId actualBookId = bookService.remove(bookId);
        assertThat(actualBookId)
                .isNotNull()
                .isEqualTo(bookId);
    }
}