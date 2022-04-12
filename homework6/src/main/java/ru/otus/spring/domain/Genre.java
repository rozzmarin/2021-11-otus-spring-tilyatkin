package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "GENRE")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(type = "ru.otus.spring.repository.hibernate.type.GenreIdType", parameters = @org.hibernate.annotations.Parameter(name = "idColumn", value = "GENRE_ID"))
    @Column(name = "GENRE_ID", nullable = false)
    private GenreId genreId;

    @Column(name = "TITLE", nullable = false, unique = true)
    private String title;

    public Genre(GenreId genreId) {
        this.genreId = genreId;
    }
}
