package ru.otus.spring.repository.specification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class GenreSpecification implements BaseSpecification<Genre> {
    private final GenreFilter filter;

    @Override
    public Predicate toPredicate(Path<Genre> path, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.isGenreIdsSpecified()) {
            predicates.add(path.get("genreId").in(filter.getGenreIds()));
        }
        if (filter.isTitleSpecified()) {
            predicates.add(criteriaBuilder.like(path.get("title"), StringUtils.quoted(filter.getTitle(), '%')));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public static GenreSpecification of(GenreFilter filter) {
        return new GenreSpecification(filter);
    }
}