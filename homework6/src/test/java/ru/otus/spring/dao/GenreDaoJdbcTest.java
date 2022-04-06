package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.dao.exception.ObjectNotFoundException;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
public class GenreDaoJdbcTest {
    private static final Genre genre1 = new Genre(new GenreId(1), "Роман");
    private static final Genre genre1ToEdit = new Genre(new GenreId(1), "Роман в стихах");
    private static final Genre genre1AfterEdit = new Genre(new GenreId(1), "Роман в стихах");
    private static final Genre genre2 = new Genre(new GenreId(2), "Поэма");
    private static final Genre genre3 = new Genre(new GenreId(3), "Эпопея");
    private static final Genre genre4ToAdd = new Genre("Повесть");
    private static final Genre genre4AfterAdd = new Genre(new GenreId(4), "Повесть");

    @Autowired
    private GenreDaoJdbc genreDao;

    @ComponentScan("ru.otus.spring.dao")
    @Configuration
    static class NestedConfiguration {
    }

    @Test
    void shouldReturnAllGenresCount() {
        long expectedGenresCount = genreDao.count(GenreFilter.builder().build());
        assertThat(expectedGenresCount)
                .isEqualTo(3);
    }

    @Test
    void shouldReturnFilteredGenresCount() {
        long expectedGenresCount = genreDao.count(GenreFilter.builder()
                .title("Роман")
                .build());
        assertThat(expectedGenresCount)
                .isEqualTo(1);
    }

    @Test
    void shouldReturnGenre() {
        Genre actualGenre = genreDao.get(new GenreId(1));
        assertThat(actualGenre)
                .usingRecursiveComparison()
                .isEqualTo(genre1);
    }

    @Test
    void shouldReturnAllGenres() {
        List<Genre> actualGenres = genreDao.get(GenreFilter.builder().build());
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1, genre2, genre3));
    }

    @Test
    void shouldReturnFilteredGenres() {
        List<Genre> actualGenres = genreDao.get(GenreFilter.builder()
                .title("Роман")
                .build());
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1));
    }

    @Test
    void shouldInsertGenre() {
        GenreId actualGenreId = genreDao.insert(genre4ToAdd);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genre4AfterAdd.getGenreId());
        Genre actualGenre = genreDao.get(actualGenreId);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre4AfterAdd);
    }

    @Test
    void shouldUpdateGenre() {
        GenreId actualGenreId = genreDao.update(genre1ToEdit);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genre1AfterEdit.getGenreId());
        Genre actualGenre = genreDao.get(actualGenreId);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre1AfterEdit);
    }

    @Test
    void shouldDeleteGenreWithoutBooks() {
        GenreId genreId = new GenreId(2);
        Genre actualGenre = genreDao.get(genreId);
        assertThat(actualGenre)
                .isNotNull();
        GenreId actualGenreId = genreDao.delete(genreId);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genreId);
        assertThatThrownBy(() -> genreDao.get(genreId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void shouldNotDeleteGenreWithBooks() {
        GenreId genreId = new GenreId(1);
        Genre actualGenre = genreDao.get(genreId);
        assertThat(actualGenre)
                .isNotNull();
        assertThatThrownBy(() -> genreDao.delete(genreId));
    }
}