package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthorServiceImplTest {
    private static final Author author1 = new Author(new AuthorId(1), "Пушкин", "Александр Сергеевич");
    private static final Author author2 = new Author(new AuthorId(2), "Толстой", "Лев Николаевич");
    private static final Author author2ToEdit = new Author(new AuthorId(2), "Толстой", "Алексей Константинович");
    private static final Author author2AfterEdit = new Author(new AuthorId(2), "Толстой", "Алексей Константинович");
    private static final Author author3 = new Author(new AuthorId(3), "Тургенев", "Иван Сергеевич");
    private static final Author author4ToAdd = new Author("Лермонтов", "Михаил Юрьевич");
    private static final Author author4AfterAdd = new Author(new AuthorId(4), "Лермонтов", "Михаил Юрьевич");

    @MockBean
    private AuthorDao authorDao;
    @Autowired
    private AuthorServiceImpl authorService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        AuthorServiceImpl authorService(AuthorDao authorDao) {
            return new AuthorServiceImpl(authorDao);
        }
    }

    @Test
    void shouldReturnAuthor() {
        AuthorId authorId = new AuthorId(1);
        given(authorDao.get(authorId))
                .willReturn(author1);
        Author actualAuthor = authorService.find(authorId);
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(author1);
    }

    @Test
    void shouldReturnAuthors() {
        AuthorFilter filter = AuthorFilter.builder().build();
        given(authorDao.get(filter))
                .willReturn(List.of(author1, author2, author3));
        List<Author> actualAuthors = authorService.find(filter);
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1, author2, author3));
    }

    @Test
    void shouldInsertAuthor() {
        given(authorDao.insert(author4ToAdd))
                .willReturn(new AuthorId(3));
        given(authorDao.get(new AuthorId(3)))
                .willReturn(author4AfterAdd);
        Author actualAuthor = authorService.add(author4ToAdd);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author4AfterAdd);
    }

    @Test
    void shouldUpdateAuthor() {
        given(authorDao.update(author2ToEdit))
                .willReturn(new AuthorId(2));
        given(authorDao.get(new AuthorId(2)))
                .willReturn(author2AfterEdit);
        Author actualAuthor = authorService.edit(author2ToEdit);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author2AfterEdit);
    }

    @Test
    void shouldDeleteAuthor() {
        AuthorId authorId = new AuthorId(1);
        given(authorDao.delete(authorId))
                .willReturn(authorId);
        AuthorId actualAuthorId = authorService.remove(authorId);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(authorId);
    }
}