package ru.otus.spring.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, AuthorId>, QuerydslPredicateExecutor<Author> {
    List<Author> findAll(Predicate predicate);
}
