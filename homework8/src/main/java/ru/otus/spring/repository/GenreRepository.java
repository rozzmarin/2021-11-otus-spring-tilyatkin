package ru.otus.spring.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreId;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, GenreId>, QuerydslPredicateExecutor<Genre> {
    List<Genre> findAll(Predicate predicate);
}
