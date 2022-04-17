package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.*;
import ru.otus.spring.util.StringUtils;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count(BookFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect count(distinct b)")
                .append("\nfrom Book b");
        if (filter.isAuthorFilterSpecified()) {
            sql.append("\n  join b.authors a");
        }
        if (filter.isGenreFilterSpecified()) {
            sql.append("\n  join b.genres g");
        }
        sql.append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        Query query = entityManager.createQuery(sql.toString());
        parametersMap.forEach(query::setParameter);
        return (long)query.getSingleResult();
    }

    @Override
    public Book get(BookId id) {
        Book book = entityManager.find(
                Book.class,
                id,
                Map.of("javax.persistence.fetchgraph", entityManager.getEntityGraph("book-entity-graph-with-authors-and-genres")));
        if (book == null) {
            throw new ObjectNotFoundException(String.format("Book with id %s is not found", id.getBookId()));
        }
        return book;
    }

    @Override
    public List<Book> get(BookFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect distinct b")
                .append("\nfrom Book b");
        if (filter.isAuthorFilterSpecified()) {
            sql.append("\n  join b.authors a");
        }
        if (filter.isGenreFilterSpecified()) {
            sql.append("\n  join b.genres g");
        }
        sql.append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        TypedQuery<Book> query = entityManager.createQuery(sql.toString(), Book.class);
        parametersMap.forEach(query::setParameter);
        List<Book> books = query.getResultList();
        books.forEach(this::initializeRelations);
        return books;
    }

    @Override
    public BookId insert(Book book) {
        entityManager.persist(book);
        entityManager.flush();
        return book.getBookId();
    }

    @Override
    public BookId update(Book book) {
        book = entityManager.merge(book);
        return book.getBookId();
    }

    @Override
    public BookId delete(BookId id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
            entityManager.flush();
            return book.getBookId();
        }
        return null;
    }

    private void createFilter(StringBuilder sql, Map<String, Object> parametersMap, BookFilter filter) {
        if (filter.isBookIdsSpecified()) {
            String bookIdsString = filter.getBookIds()
                    .stream()
                    .map(id ->  Long.toString(id.getBookId()))
                    .collect(Collectors.joining(","));
            sql.append("\n  and b.bookId in");
            sql.append("\n  (");
            sql.append(bookIdsString);
            sql.append("\n  )");
        }
        if (filter.isTitleSpecified()) {
            sql.append("\n  and b.title like :title");
            parametersMap.put("title", StringUtils.quoted(filter.getTitle(), '%'));
        }
        if (filter.isReviewsCountSpecified()) {
            sql.append("\n  and (select count(br) from BookReview br where br.book.bookId = b.bookId) >= :reviewsCount");
            parametersMap.put("reviewsCount", filter.getReviewsCount());
        }
        if (filter.isAuthorFilterSpecified()) {
            if (filter.getAuthorFilter().isAuthorIdsSpecified()) {
                String authorIdsString = filter.getAuthorFilter().getAuthorIds()
                        .stream()
                        .map(id -> Long.toString(id.getAuthorId()))
                        .collect(Collectors.joining(","));
                sql.append("\n  and a.authorId in");
                sql.append("\n  (");
                sql.append(authorIdsString);
                sql.append("\n  )");
            }
            if (filter.getAuthorFilter().isNameSpecified()) {
                sql.append("\n  and (a.lastname like :name or a.firstname like :name)");
                parametersMap.put("name", StringUtils.quoted(filter.getAuthorFilter().getName(), '%'));
            }
        }
        if (filter.isGenreFilterSpecified()) {
            if (filter.getGenreFilter().isGenreIdsSpecified()) {
                String genreIdsString = filter.getGenreFilter().getGenreIds()
                        .stream()
                        .map(id ->  Long.toString(id.getGenreId()))
                        .collect(Collectors.joining(","));
                sql.append("\n  and g.genreId in");
                sql.append("\n  (");
                sql.append(genreIdsString);
                sql.append("\n  )");
            }
            if (filter.getGenreFilter().isTitleSpecified()) {
                sql.append("\n  and g.title like :title");
                parametersMap.put("title", StringUtils.quoted(filter.getGenreFilter().getTitle(), '%'));
            }
        }
    }

    private void initializeRelations(Book book) {
        Hibernate.initialize(book.getAuthors());
        Hibernate.initialize(book.getGenres());
    }
}