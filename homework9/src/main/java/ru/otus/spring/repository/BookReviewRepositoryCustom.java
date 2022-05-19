package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.domain.BookId;
import ru.otus.spring.domain.BookReview;
import ru.otus.spring.domain.BookReviewId;

import java.util.Map;
import java.util.Set;

public interface BookReviewRepositoryCustom {
    Map<BookId, Long> countAtBooks(Set<BookId> bookIds);
}
