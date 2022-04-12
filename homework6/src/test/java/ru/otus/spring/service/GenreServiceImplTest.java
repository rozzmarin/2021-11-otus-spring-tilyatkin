package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GenreServiceImplTest {
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

    @MockBean
    private GenreRepository genreRepository;
    @Autowired
    private GenreServiceImpl genreService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        GenreServiceImpl genreService(GenreRepository genreRepository) {
            return new GenreServiceImpl(genreRepository);
        }
    }

    @Test
    void shouldReturnGenre() {
        GenreId genreId = new GenreId(1);
        given(genreRepository.get(genreId))
                .willReturn(genre1);

        Genre actualGenre = genreService.find(genreId);
        assertThat(actualGenre)
                .usingRecursiveComparison()
                .isEqualTo(genre1);
    }

    @Test
    void shouldReturnGenres() {
        GenreFilter filter = GenreFilter.builder().build();
        given(genreRepository.get(filter))
                .willReturn(List.of(genre1, genre2));

        List<Genre> actualGenres = genreService.find(filter);
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(genre1, genre2));
    }

    @Test
    void shouldInsertGenre() {
        given(genreRepository.insert(genre4ToAdd))
                .willReturn(new GenreId(3));
        given(genreRepository.get(new GenreId(3)))
                .willReturn(genre4AfterAdd);

        Genre actualGenre = genreService.add(genre4ToAdd);
        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre4AfterAdd);
    }

    @Test
    void shouldUpdateGenre() {
        given(genreRepository.update(genre1ToEdit))
                .willReturn(new GenreId(1));
        given(genreRepository.get(new GenreId(1)))
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
        given(genreRepository.delete(genreId))
                .willReturn(genreId);

        GenreId actualGenreId = genreService.remove(genreId);
        assertThat(actualGenreId)
                .isNotNull()
                .isEqualTo(genreId);
    }
}