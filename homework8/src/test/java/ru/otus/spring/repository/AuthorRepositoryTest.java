package ru.otus.spring.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.repository.specification.AuthorSpecification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@Import(AuthorSpecification.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRepositoryTest {
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
    private static final Author author2ToEdit = Author.builder()
            .authorId(new AuthorId("A002"))
            .lastname("Толстой")
            .firstname("Алексей Константинович")
            .build();
    private static final Author author2AfterEdit = author2ToEdit;
    private static final Author author3 = Author.builder()
            .authorId(new AuthorId("A003"))
            .lastname("Тургенев")
            .firstname("Иван Сергеевич")
            .build();
    private static final Author author4ToAdd = Author.builder()
            .authorId(new AuthorId("A004"))
            .lastname("Лермонтов")
            .firstname("Михаил Юрьевич")
            .build();
    private static final Author author4AfterAdd = author4ToAdd;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorSpecification authorSpecification;

    @Test
    @Order(1)
    void shouldReturnAllAuthorsCount() {
        long expectedAuthorsCount = authorRepository.count(authorSpecification.toPredicate(AuthorFilter.builder().build()));
        assertThat(expectedAuthorsCount)
                .isEqualTo(3);
    }

    @Test
    @Order(1)
    void shouldReturnFilteredAuthorsCount() {
        long expectedAuthorsCount = authorRepository.count(authorSpecification.toPredicate(AuthorFilter.builder()
                .name("Сергеевич")
                .build()));
        assertThat(expectedAuthorsCount)
                .isEqualTo(2);
    }

    @Test
    @Order(1)
    void shouldReturnAuthor() {
        Optional<Author> actualAuthor = authorRepository.findById(new AuthorId("A001"));
        assertThat(actualAuthor)
                .isNotEmpty()
                .hasValue(author1);
    }

    @Test
    @Order(1)
    void shouldReturnAllAuthors() {
        List<Author> actualAuthors = authorRepository.findAll(authorSpecification.toPredicate(AuthorFilter.builder().build()));
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1, author2, author3));
    }

    @Test
    @Order(1)
    void shouldReturnFilteredAuthors() {
        List<Author> actualAuthors = authorRepository.findAll(authorSpecification.toPredicate(AuthorFilter.builder()
                .name("Александр")
                .build()));
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1));
    }

    @Test
    @Order(2)
    void shouldInsertAuthor() {
        Author actualAuthor = authorRepository.save(author4ToAdd);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author4AfterAdd);
    }

    @Test
    @Order(3)
    void shouldUpdateAuthor() {
        Author actualAuthor = authorRepository.save(author2ToEdit);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author2AfterEdit);
    }

    @Test
    @Order(4)
    void shouldDeleteAuthor() {
        AuthorId authorId = new AuthorId("A003");
        assertThat(authorRepository.findById(authorId))
                .isNotEmpty();
        authorRepository.deleteById(authorId);
        assertThat(authorRepository.findById(authorId))
                .isEmpty();
    }
}