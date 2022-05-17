package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenreDto {
    private GenreId genreId;

    @NotNull(message = "Наименование не может быть пустым")
    @Size(min = 1, max = 255, message = "Наименование должно быть длиной от 1 до 255 символов")
    private String title;

    public Genre toDomain() {
        return Genre.builder()
                .genreId(genreId)
                .title(title)
                .build();
    }

    public static GenreDto fromDomain(Genre genre) {
        return new GenreDto(
                genre.getGenreId(),
                genre.getTitle());
    }
}
