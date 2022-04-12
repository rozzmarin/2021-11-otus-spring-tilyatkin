package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count(AuthorFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect count(a)")
                .append("\nfrom Author a")
                .append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        Query query = entityManager.createQuery(sql.toString());
        parametersMap.forEach(query::setParameter);
        return (long)query.getSingleResult();
    }

    @Override
    public Author get(AuthorId id) {
        Author author = entityManager.find(Author.class, id);
        if (author == null) {
            throw new ObjectNotFoundException(String.format("Author with id %s is not found", id.getAuthorId()));
        }
        return author;
    }

    @Override
    public List<Author> get(AuthorFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect a")
                .append("\nfrom Author a")
                .append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        TypedQuery<Author> query = entityManager.createQuery(sql.toString(), Author.class);
        parametersMap.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public AuthorId insert(Author author) {
        entityManager.persist(author);
        entityManager.flush();
        return author.getAuthorId();
    }

    @Override
    public AuthorId update(Author author) {
        author = entityManager.merge(author);
        return author.getAuthorId();
    }

    @Override
    public AuthorId delete(AuthorId id) {
        Author author = entityManager.find(Author.class, id);
        if (author != null) {
            entityManager.remove(author);
            entityManager.flush();
            return author.getAuthorId();
        }
        return null;
    }

    private void createFilter(StringBuilder sql, Map<String, Object> parametersMap, AuthorFilter filter) {
        if (filter.isAuthorIdsSpecified()) {
            String authorIdsString = filter.getAuthorIds()
                    .stream()
                    .map(id ->  Long.toString(id.getAuthorId()))
                    .collect(Collectors.joining(","));
            sql.append("\n  and a.authorId in");
            sql.append("\n  (");
            sql.append(authorIdsString);
            sql.append("\n  )");
        }
        if (filter.isNameSpecified()) {
            sql.append("\n  and (a.lastname like :name or a.firstname like :name)");
            parametersMap.put("name", StringUtils.quoted(filter.getName(), '%'));
        }
    }
}