package ru.otus.spring.repository.specification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AuthorSpecification implements BaseSpecification<Author> {
    private final AuthorFilter filter;

    @Override
    public Predicate toPredicate(Path<Author> path, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.isAuthorIdsSpecified()) {
            predicates.add(path.get("authorId").in(filter.getAuthorIds()));
        }
        if (filter.isNameSpecified()) {
            String pattern = StringUtils.quoted(filter.getName(), '%');
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(path.get("lastname"), pattern),
                    criteriaBuilder.like(path.get("firstname"), pattern)));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public static AuthorSpecification of(AuthorFilter filter) {
        return new AuthorSpecification(filter);
    }
}