package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.repository.specification.AuthorSpecification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class AuthorRepositoryJpaTest {
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
    private static final Author author2ToEdit = Author.builder()
            .authorId(new AuthorId(2))
            .lastname("Толстой")
            .firstname("Алексей Константинович")
            .build();
    private static final Author author2AfterEdit = Author.builder()
            .authorId(new AuthorId(2))
            .lastname("Толстой")
            .firstname("Алексей Константинович")
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

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnAllAuthorsCount() {
        long expectedAuthorsCount = authorRepository.count(AuthorSpecification.of(AuthorFilter.builder().build()));
        assertThat(expectedAuthorsCount)
                .isEqualTo(3);
    }

    @Test
    void shouldReturnFilteredAuthorsCount() {
        long expectedAuthorsCount = authorRepository.count(AuthorSpecification.of(AuthorFilter.builder()
                .name("Сергеевич")
                .build()));
        assertThat(expectedAuthorsCount)
                .isEqualTo(2);
    }

    @Test
    void shouldReturnAuthor() {
        Optional<Author> actualAuthor = authorRepository.findById(new AuthorId(1));
        assertThat(actualAuthor)
                .isNotEmpty()
                .hasValue(author1);
    }

    @Test
    void shouldReturnAllAuthors() {
        List<Author> actualAuthors = authorRepository.findAll(AuthorSpecification.of(AuthorFilter.builder().build()));
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1, author2, author3));
    }

    @Test
    void shouldReturnFilteredAuthors() {
        List<Author> actualAuthors = authorRepository.findAll(AuthorSpecification.of(AuthorFilter.builder()
                .name("Александр")
                .build()));
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1));
    }

    @Test
    void shouldInsertAuthor() {
        Author actualAuthor = authorRepository.saveAndFlush(author4ToAdd);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author4AfterAdd);
    }

    @Test
    void shouldUpdateAuthor() {
        Author actualAuthor = authorRepository.saveAndFlush(author2ToEdit);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author2AfterEdit);
    }

    @Test
    void shouldDeleteAuthorWithoutBooks() {
        AuthorId authorId = new AuthorId(3);
        assertThat(authorRepository.findById(authorId))
                .isNotEmpty();
        authorRepository.deleteById(authorId);
        authorRepository.flush();
        assertThat(authorRepository.findById(authorId))
                .isEmpty();
    }

    @Test
    void shouldNotDeleteAuthorWithBooks() {
        AuthorId authorId = new AuthorId(1);
        assertThat(authorRepository.findById(authorId))
                .isNotEmpty();
        assertThatThrownBy(() -> {
            authorRepository.deleteById(authorId);
            authorRepository.flush();
        });
    }
}