package ru.otus.spring.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.spring.domain.*;

import java.util.List;
import java.util.Optional;

public interface BookReviewRepository extends JpaRepository<BookReview, BookReviewId>, JpaSpecificationExecutor<BookReview>, BookReviewRepositoryCustom {
    @EntityGraph("book-review-entity-graph-with-book-authors")
    Optional<BookReview> findById(BookReviewId id);

    @EntityGraph("book-review-entity-graph-with-book-authors")
    List<BookReview> findAll(Specification<BookReview> spec);
}
