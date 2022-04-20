package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "books")
public class Book {
    @Id
    private BookId bookId;
    private String title;
    private Set<Author> authors;
    private Set<Genre> genres;
    @Transient
    private long reviewsCount;

    public Book(BookId bookId) {
        this.bookId = bookId;
    }
}
