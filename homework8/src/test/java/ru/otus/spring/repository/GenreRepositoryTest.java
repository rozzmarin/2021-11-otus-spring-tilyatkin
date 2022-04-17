package ru.otus.spring.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;
import ru.otus.spring.repository.specification.GenreSpecification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import(GenreSpecification.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenreRepositoryTest {
    private static final Genre genre1 = Genre.builder()
            .genreId(new GenreId("G001"))
            .title("Роман")
            .build();
    private static final Genre genre1ToEdit = Genre.builder()
            .genreId(new GenreId("G001"))
            .title("Роман в стихах")
            .build();
    private static final Genre genre1AfterEdit = genre1ToEdit;
    private static final Genre genre2 = Genre.builder()
            .genreId(new GenreId("G002"))
            .title("Поэма")
            .build();
    private static final Genre genre3 = Genre.builder()
            .genreId(new GenreId("G003"))
            .title("Эпопея")
            .build();
    private static final Genre genre4ToAdd = Genre.builder()
            .genreId(new GenreId("G004"))
            .title("Повесть")
            .build();
    private static final Genre genre4AfterAdd = genre4ToAdd;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreSpecification genreSpecification;

    @Test
    @Order(1)
    void shouldReturnAllGenresCount() {
        long expectedGenresCount = genreRepository.count(genreSpecification.toPredicate(GenreFilter.builder().build()));
        assertThat(expectedGenresCount)
                .isEqualTo(3);
    }

    @Test
    @Order(1)
    void shouldReturnFilteredGenresCount() {
        long expectedGenresCount = genreRepository.count(genreSpecification.toPredicate(GenreFilter.builder()
                .title("Роман")
                .build()));
        assertThat(expectedGenresCount)
                .isEqualTo(1);
    }

    @Test
    @Order(1)
    void shouldReturnGenre() {
        Optional<Genre> actualGenre = genreRepository.findById(new GenreId("G001"));
        assertThat(actualGenre)
                .isNotEmpty()
                .hasValue(genre1);
    }

    @Test
    @Order(1)
    void shouldReturnAllGenres() {
        List<Genre> actualGenres = genreRepository.findAll(genreSpecification.toPredicate(GenreFilter.builder().build()));
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1, genre2, genre3));
    }

    @Test
    @Order(1)
    void shouldReturnFilteredGenres() {
        List<Genre> actualGenres = genreRepository.findAll(genreSpecification.toPredicate(GenreFilter.builder()
                .title("Роман")
                .build()));
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1));
    }

    @Test
    @Order(2)
    void shouldInsertGenre() {
        Genre actualGenre = genreRepository.save(genre4ToAdd);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre4AfterAdd);
    }

    @Test
    @Order(3)
    void shouldUpdateGenre() {
        Genre actualGenre = genreRepository.save(genre1ToEdit);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre1AfterEdit);
    }

    @Test
    @Order(4)
    void shouldDeleteGenre() {
        GenreId genreId = new GenreId("G002");
        assertThat(genreRepository.findById(genreId))
                .isNotEmpty();
        genreRepository.deleteById(genreId);
        assertThat(genreRepository.findById(genreId))
                .isEmpty();
    }
}