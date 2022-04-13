package ru.otus.spring.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public interface BaseSpecification<T> extends Specification<T> {
    Predicate toPredicate(Path<T> path, CriteriaBuilder criteriaBuilder);

    default Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return toPredicate(root, criteriaBuilder);
    }
}