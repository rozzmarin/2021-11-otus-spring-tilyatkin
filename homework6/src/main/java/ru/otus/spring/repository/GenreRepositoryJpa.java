package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.domain.*;
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
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count(GenreFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect count(g)")
                .append("\nfrom Genre g")
                .append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        Query query = entityManager.createQuery(sql.toString());
        parametersMap.forEach(query::setParameter);
        return (long)query.getSingleResult();
    }

    @Override
    public Genre get(GenreId id) {
        Genre genre = entityManager.find(Genre.class, id);
        if (genre == null) {
            throw new ObjectNotFoundException(String.format("Genre with id %s is not found", id.getGenreId()));
        }
        return genre;
    }

    @Override
    public List<Genre> get(GenreFilter filter) {
        StringBuilder sql = new StringBuilder()
                .append("\nselect g")
                .append("\nfrom Genre g")
                .append("\nwhere 1 = 1");
        Map<String, Object> parametersMap = new HashMap<>();
        createFilter(sql, parametersMap, filter);
        TypedQuery<Genre> query = entityManager.createQuery(sql.toString(), Genre.class);
        parametersMap.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public GenreId insert(Genre genre) {
        entityManager.persist(genre);
        entityManager.flush();
        return genre.getGenreId();
    }

    @Override
    public GenreId update(Genre genre) {
        genre = entityManager.merge(genre);
        return genre.getGenreId();
    }

    @Override
    public GenreId delete(GenreId id) {
        Genre genre = entityManager.find(Genre.class, id);
        if (genre != null) {
            entityManager.remove(genre);
            entityManager.flush();
            return genre.getGenreId();
        }
        return null;
    }

    private void createFilter(StringBuilder sql, Map<String, Object> parametersMap, GenreFilter filter) {
        if (filter.isGenreIdsSpecified()) {
            String genreIdsString = filter.getGenreIds()
                    .stream()
                    .map(id ->  Long.toString(id.getGenreId()))
                    .collect(Collectors.joining(","));
            sql.append("\n  and g.genreId in");
            sql.append("\n  (");
            sql.append(genreIdsString);
            sql.append("\n  )");
        }
        if (filter.isTitleSpecified()) {
            sql.append("\n  and g.title like :title");
            parametersMap.put("title", StringUtils.quoted(filter.getTitle(), '%'));
        }
    }
}