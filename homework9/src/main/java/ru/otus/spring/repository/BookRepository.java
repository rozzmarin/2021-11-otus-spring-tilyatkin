package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.spring.domain.*;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, BookId>, JpaSpecificationExecutor<Book> {
    @EntityGraph("book-entity-graph-with-authors-and-genres")
    Optional<Book> findById(BookId id);

    Boolean existsByAuthorsAuthorId(AuthorId id);

    Boolean existsByGenresGenreId(GenreId id);
}