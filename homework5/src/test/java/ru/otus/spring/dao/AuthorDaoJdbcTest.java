package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.dao.exception.ObjectNotFoundException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
public class AuthorDaoJdbcTest {
    private static final Author author1 = new Author(new AuthorId(1), "Пушкин", "Александр Сергеевич");
    private static final Author author2 = new Author(new AuthorId(2), "Толстой", "Лев Николаевич");
    private static final Author author2ToEdit = new Author(new AuthorId(2), "Толстой", "Алексей Константинович");
    private static final Author author2AfterEdit = new Author(new AuthorId(2), "Толстой", "Алексей Константинович");
    private static final Author author3 = new Author(new AuthorId(3), "Тургенев", "Иван Сергеевич");
    private static final Author author4ToAdd = new Author("Лермонтов", "Михаил Юрьевич");
    private static final Author author4AfterAdd = new Author(new AuthorId(4), "Лермонтов", "Михаил Юрьевич");

    @Autowired
    private AuthorDaoJdbc authorDao;

    @ComponentScan("ru.otus.spring.dao")
    @Configuration
    static class NestedConfiguration {
    }

    @Test
    void shouldReturnAllAuthorsCount() {
        long expectedAuthorsCount = authorDao.count(AuthorFilter.builder().build());
        assertThat(expectedAuthorsCount)
                .isEqualTo(3);
    }

    @Test
    void shouldReturnFilteredAuthorsCount() {
        long expectedAuthorsCount = authorDao.count(AuthorFilter.builder()
                .name("Сергеевич")
                .build());
        assertThat(expectedAuthorsCount)
                .isEqualTo(2);
    }

    @Test
    void shouldReturnAuthor() {
        Author actualAuthor = authorDao.get(new AuthorId(1));
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(author1);
    }

    @Test
    void shouldReturnAllAuthors() {
        List<Author> actualAuthors = authorDao.get(AuthorFilter.builder().build());
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1, author2, author3));
    }

    @Test
    void shouldReturnFilteredAuthors() {
        List<Author> actualAuthors = authorDao.get(AuthorFilter.builder()
                .name("Александр")
                .build());
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1));
    }

    @Test
    void shouldInsertAuthor() {
        AuthorId actualAuthorId = authorDao.insert(author4ToAdd);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(author4AfterAdd.getAuthorId());
        Author actualAuthor = authorDao.get(actualAuthorId);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author4AfterAdd);
    }

    @Test
    void shouldUpdateAuthor() {
        AuthorId actualAuthorId = authorDao.update(author2ToEdit);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(author2AfterEdit.getAuthorId());
        Author actualAuthor = authorDao.get(actualAuthorId);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author2AfterEdit);
    }

    @Test
    void shouldDeleteAuthorWithoutBooks() {
        AuthorId authorId = new AuthorId(3);
        Author actualAuthor = authorDao.get(authorId);
        assertThat(actualAuthor)
                .isNotNull();
        AuthorId actualAuthorId = authorDao.delete(authorId);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(authorId);
        assertThatThrownBy(() -> authorDao.get(authorId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void shouldNotDeleteAuthorWithBooks() {
        AuthorId authorId = new AuthorId(1);
        Author actualAuthor = authorDao.get(authorId);
        assertThat(actualAuthor)
                .isNotNull();
        assertThatThrownBy(() -> authorDao.delete(authorId));
    }
}