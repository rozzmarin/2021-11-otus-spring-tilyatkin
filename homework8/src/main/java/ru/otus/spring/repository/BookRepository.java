package ru.otus.spring.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookRepository extends MongoRepository<Book, BookId>, QuerydslPredicateExecutor<Book> {
    @Query(fields="{ '_id' : 1, 'title' : 1, 'authors' : 1 }")
    Optional<Book> findTitleAndAuthorsByBookId(BookId id);

    @Query(fields="{ '_id' : 1, 'title' : 1, 'authors' : 1 }")
    List<Book> findTitleAndAuthorsByBookIdIn(Set<BookId> ids);

    List<Book> findAll(Predicate predicate);
}