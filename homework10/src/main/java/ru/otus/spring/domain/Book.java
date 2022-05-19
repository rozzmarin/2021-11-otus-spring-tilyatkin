package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "BOOK")
@NamedEntityGraph(
        name = "book-entity-graph-with-authors-and-genres",
        attributeNodes = {
                @NamedAttributeNode("authors"),
                @NamedAttributeNode("genres")
        })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(type = "ru.otus.spring.repository.hibernate.type.BookIdType", parameters = @org.hibernate.annotations.Parameter(name = "idColumn", value = "BOOK_ID"))
    @Column(name = "BOOK_ID", nullable = false)
    private BookId bookId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BOOK_AUTHOR", joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false), inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID", nullable = false))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BOOK_GENRE", joinColumns = @JoinColumn(name = "BOOK_ID", nullable = false), inverseJoinColumns = @JoinColumn(name = "GENRE_ID", nullable = false))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Genre> genres;

    @Transient
    private long reviewsCount;

    public Book(BookId bookId) {
        this.bookId = bookId;
    }
}
