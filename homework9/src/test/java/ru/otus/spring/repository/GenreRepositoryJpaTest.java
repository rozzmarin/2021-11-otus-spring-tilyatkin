package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;
import ru.otus.spring.repository.specification.GenreSpecification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class GenreRepositoryJpaTest {
    private static final Genre genre1 = Genre.builder()
            .genreId(new GenreId(1))
            .title("Роман")
            .build();
    private static final Genre genre1ToEdit = Genre.builder()
            .genreId(new GenreId(1))
            .title("Роман в стихах")
            .build();
    private static final Genre genre1AfterEdit = Genre.builder()
            .genreId(new GenreId(1))
            .title("Роман в стихах")
            .build();
    private static final Genre genre2 = Genre.builder()
            .genreId(new GenreId(2))
            .title("Поэма")
            .build();
    private static final Genre genre3 = Genre.builder()
            .genreId(new GenreId(3))
            .title("Эпопея")
            .build();
    private static final Genre genre4ToAdd = Genre.builder()
            .title("Повесть")
            .build();
    private static final Genre genre4AfterAdd = Genre.builder()
            .genreId(new GenreId(4))
            .title("Повесть")
            .build();

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldReturnAllGenresCount() {
        long expectedGenresCount = genreRepository.count(GenreSpecification.of(GenreFilter.builder().build()));
        assertThat(expectedGenresCount)
                .isEqualTo(3);
    }

    @Test
    void shouldReturnFilteredGenresCount() {
        long expectedGenresCount = genreRepository.count(GenreSpecification.of(GenreFilter.builder()
                .title("Роман")
                .build()));
        assertThat(expectedGenresCount)
                .isEqualTo(1);
    }

    @Test
    void shouldReturnGenre() {
        Optional<Genre> actualGenre = genreRepository.findById(new GenreId(1));
        assertThat(actualGenre)
                .isNotEmpty()
                .hasValue(genre1);
    }

    @Test
    void shouldReturnAllGenres() {
        List<Genre> actualGenres = genreRepository.findAll(GenreSpecification.of(GenreFilter.builder().build()));
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1, genre2, genre3));
    }

    @Test
    void shouldReturnFilteredGenres() {
        List<Genre> actualGenres = genreRepository.findAll(GenreSpecification.of(GenreFilter.builder()
                .title("Роман")
                .build()));
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1));
    }

    @Test
    void shouldInsertGenre() {
        Genre actualGenre = genreRepository.saveAndFlush(genre4ToAdd);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre4AfterAdd);
    }

    @Test
    void shouldUpdateGenre() {
        Genre actualGenre = genreRepository.saveAndFlush(genre1ToEdit);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre1AfterEdit);
    }

    @Test
    void shouldDeleteGenreWithoutBooks() {
        GenreId genreId = new GenreId(2);
        assertThat(genreRepository.findById(genreId))
                .isNotEmpty();
        genreRepository.deleteById(genreId);
        genreRepository.flush();
        assertThat(genreRepository.findById(genreId))
                .isEmpty();
    }

    @Test
    void shouldNotDeleteGenreWithBooks() {
        GenreId genreId = new GenreId(1);
        assertThat(genreRepository.findById(genreId))
                .isNotEmpty();
        assertThatThrownBy(() -> {
            genreRepository.deleteById(genreId);
            genreRepository.flush();
        });
    }
}