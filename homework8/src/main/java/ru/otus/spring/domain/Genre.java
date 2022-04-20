package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(value = "genres")
public class Genre {
    @Id
    private GenreId genreId;
    private String title;

    public Genre(GenreId genreId) {
        this.genreId = genreId;
    }
}
