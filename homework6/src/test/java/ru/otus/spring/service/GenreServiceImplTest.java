package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GenreServiceImplTest {
    private static final Genre genre1 = new Genre(new GenreId(1), "Роман");
    private static final Genre genre1ToEdit = new Genre(new GenreId(1), "Роман в стихах");
    private static final Genre genre1AfterEdit = new Genre(new GenreId(1), "Роман в стихах");
    private static final Genre genre2 = new Genre(new GenreId(2), "Поэма");
    private static final Genre genre3 = new Genre(new GenreId(3), "Эпопея");
    private static final Genre genre4ToAdd = new Genre("Повесть");
    private static final Genre genre4AfterAdd = new Genre(new GenreId(4), "Повесть");

    @MockBean
    private GenreDao genreDao;
    @Autowired
    private GenreServiceImpl genreService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        GenreServiceImpl genreService(GenreDao genreDao) {
            return new GenreServiceImpl(genreDao);
        }
    }

    @Test
    void shouldReturnGenre() {
        GenreId genreId = new GenreId(1);
        given(genreDao.get(genreId))
                .willReturn(genre1);
        Genre actualGenre = genreService.find(genreId);
        assertThat(actualGenre)
                .usingRecursiveComparison()
                .isEqualTo(genre1);
    }

    @Test
    void shouldReturnGenres() {
        GenreFilter filter = GenreFilter.builder().build();
        given(genreDao.get(filter))
                .willReturn(List.of(genre1, genre2));
        List<Genre> actualGenres = genreService.find(filter);
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1, genre2));
    }

    @Test
    void shouldInsertGenre() {
        given(genreDao.insert(genre4ToAdd))
                .willReturn(new GenreId(3));
        given(genreDao.get(new GenreId(3)))
                .willReturn(genre4AfterAdd);
        Genre actualGenre = genreService.add(genre4ToAdd);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre4AfterAdd);
    }

    @Test
    void shouldUpdateGenre() {
        given(genreDao.update(genre1ToEdit))
                .willReturn(new GenreId(1));
        given(genreDao.get(new GenreId(1)))
                .willReturn(genre1AfterEdit);
        Genre actualGenre = genreService.edit(genre1ToEdit);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre1AfterEdit);
    }

    @Test
    void shouldDeleteGenre() {
        GenreId genreId = new GenreId(1);
        given(genreDao.delete(genreId))
                .willReturn(genreId);
        GenreId actualGenreId = genreService.remove(genreId);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genreId);
    }
}