package ru.otus.spring.repository.specification;

import com.querydsl.core.types.Predicate;

public interface BaseSpecification<T> {
    Predicate toPredicate(T filter);
}
