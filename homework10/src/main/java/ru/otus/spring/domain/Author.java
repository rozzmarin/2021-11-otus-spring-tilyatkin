package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "AUTHOR")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(type = "ru.otus.spring.repository.hibernate.type.AuthorIdType", parameters = @Parameter(name = "idColumn", value = "AUTHOR_ID"))
    @Column(name = "AUTHOR_ID", nullable = false)
    private AuthorId authorId;

    @Column(name = "LASTNAME", nullable = false)
    private String lastname;

    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;

    public Author(AuthorId authorId) {
        this.authorId = authorId;
    }
}
