package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(AuthorRepositoryJpa.class)
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
    private AuthorRepositoryJpa authorRepository;

    @Test
    void shouldReturnAllAuthorsCount() {
        long expectedAuthorsCount = authorRepository.count(AuthorFilter.builder().build());
        assertThat(expectedAuthorsCount)
                .isEqualTo(3);
    }

    @Test
    void shouldReturnFilteredAuthorsCount() {
        long expectedAuthorsCount = authorRepository.count(AuthorFilter.builder()
                .name("Сергеевич")
                .build());
        assertThat(expectedAuthorsCount)
                .isEqualTo(2);
    }

    @Test
    void shouldReturnAuthor() {
        Author actualAuthor = authorRepository.get(new AuthorId(1));
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(author1);
    }

    @Test
    void shouldReturnAllAuthors() {
        List<Author> actualAuthors = authorRepository.get(AuthorFilter.builder().build());
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1, author2, author3));
    }

    @Test
    void shouldReturnFilteredAuthors() {
        List<Author> actualAuthors = authorRepository.get(AuthorFilter.builder()
                .name("Александр")
                .build());
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1));
    }

    @Test
    void shouldInsertAuthor() {
        AuthorId actualAuthorId = authorRepository.insert(author4ToAdd);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(author4AfterAdd.getAuthorId());
        Author actualAuthor = authorRepository.get(actualAuthorId);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author4AfterAdd);
    }

    @Test
    void shouldUpdateAuthor() {
        AuthorId actualAuthorId = authorRepository.update(author2ToEdit);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(author2AfterEdit.getAuthorId());
        Author actualAuthor = authorRepository.get(actualAuthorId);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author2AfterEdit);
    }

    @Test
    void shouldDeleteAuthorWithoutBooks() {
        AuthorId authorId = new AuthorId(3);
        Author actualAuthor = authorRepository.get(authorId);
        assertThat(actualAuthor)
                .isNotNull();
        AuthorId actualAuthorId = authorRepository.delete(authorId);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(authorId);
        assertThatThrownBy(() -> authorRepository.get(authorId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void shouldNotDeleteAuthorWithBooks() {
        AuthorId authorId = new AuthorId(1);
        Author actualAuthor = authorRepository.get(authorId);
        assertThat(actualAuthor)
                .isNotNull();
        assertThatThrownBy(() -> authorRepository.delete(authorId));
    }
}