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
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.specification.AuthorSpecification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthorServiceImplTest {
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

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private AuthorServiceImpl authorService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        AuthorServiceImpl authorService(AuthorRepository authorRepository, BookRepository bookRepository) {
            return new AuthorServiceImpl(authorRepository, bookRepository);
        }
    }

    @Test
    void shouldReturnAuthor() {
        AuthorId authorId = new AuthorId(1);
        given(authorRepository.findById(authorId))
                .willReturn(Optional.of(author1));

        Author actualAuthor = authorService.find(authorId);
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(author1);
    }

    @Test
    void shouldReturnAuthors() {
        AuthorFilter filter = AuthorFilter.builder().build();
        given(authorRepository.findAll(AuthorSpecification.of(filter)))
                .willReturn(List.of(author1, author2, author3));

        List<Author> actualAuthors = authorService.find(filter);
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(author1, author2, author3));
    }

    @Test
    void shouldInsertAuthor() {
        given(authorRepository.save(author4ToAdd))
                .willReturn(author4AfterAdd);

        Author actualAuthor = authorService.add(author4ToAdd);
        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(author4AfterAdd);
    }

    @Test
    void shouldUpdateAuthor() {
        given(authorRepository.findById(author2ToEdit.getAuthorId()))
                .willReturn(Optional.of(author2));
        given(authorRepository.save(author2ToEdit))
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
        given(authorRepository.findById(authorId))
                .willReturn(Optional.of(author1));

        AuthorId actualAuthorId = authorService.remove(authorId);
        assertThat(actualAuthorId)
                .isNotNull()
                .isEqualTo(authorId);
    }
}