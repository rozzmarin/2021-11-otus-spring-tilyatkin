package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "authors")
public class Author {
    @Id
    private AuthorId authorId;
    private String lastname;
    private String firstname;

    public Author(AuthorId authorId) {
        this.authorId = authorId;
    }
}
