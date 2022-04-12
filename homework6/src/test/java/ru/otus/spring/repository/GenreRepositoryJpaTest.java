package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(GenreRepositoryJpa.class)
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
    private GenreRepositoryJpa genreRepository;

    @Test
    void shouldReturnAllGenresCount() {
        long expectedGenresCount = genreRepository.count(GenreFilter.builder().build());
        assertThat(expectedGenresCount)
                .isEqualTo(3);
    }

    @Test
    void shouldReturnFilteredGenresCount() {
        long expectedGenresCount = genreRepository.count(GenreFilter.builder()
                .title("Роман")
                .build());
        assertThat(expectedGenresCount)
                .isEqualTo(1);
    }

    @Test
    void shouldReturnGenre() {
        Genre actualGenre = genreRepository.get(new GenreId(1));
        assertThat(actualGenre)
                .usingRecursiveComparison()
                .isEqualTo(genre1);
    }

    @Test
    void shouldReturnAllGenres() {
        List<Genre> actualGenres = genreRepository.get(GenreFilter.builder().build());
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1, genre2, genre3));
    }

    @Test
    void shouldReturnFilteredGenres() {
        List<Genre> actualGenres = genreRepository.get(GenreFilter.builder()
                .title("Роман")
                .build());
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1));
    }

    @Test
    void shouldInsertGenre() {
        GenreId actualGenreId = genreRepository.insert(genre4ToAdd);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genre4AfterAdd.getGenreId());
        Genre actualGenre = genreRepository.get(actualGenreId);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre4AfterAdd);
    }

    @Test
    void shouldUpdateGenre() {
        GenreId actualGenreId = genreRepository.update(genre1ToEdit);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genre1AfterEdit.getGenreId());
        Genre actualGenre = genreRepository.get(actualGenreId);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre1AfterEdit);
    }

    @Test
    void shouldDeleteGenreWithoutBooks() {
        GenreId genreId = new GenreId(2);
        Genre actualGenre = genreRepository.get(genreId);
        assertThat(actualGenre)
                .isNotNull();
        GenreId actualGenreId = genreRepository.delete(genreId);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genreId);
        assertThatThrownBy(() -> genreRepository.get(genreId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void shouldNotDeleteGenreWithBooks() {
        GenreId genreId = new GenreId(1);
        Genre actualGenre = genreRepository.get(genreId);
        assertThat(actualGenre)
                .isNotNull();
        assertThatThrownBy(() -> genreRepository.delete(genreId));
    }
}