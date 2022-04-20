package ru.otus.spring.repository.specification;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.QGenre;
import ru.otus.spring.util.StringUtils;

@Component
public class GenreSpecification implements BaseSpecification<GenreFilter> {
    @Override
    public Predicate toPredicate(GenreFilter filter) {
        BooleanBuilder expressionBuilder = new BooleanBuilder();
        if (filter.isGenreIdsSpecified()) {
            expressionBuilder.and(QGenre.genre.genreId.in(filter.getGenreIds()));
        }
        if (filter.isTitleSpecified()) {
            expressionBuilder.and(QGenre.genre.title.like(StringUtils.quoted(filter.getTitle(), '%')));
        }
        return expressionBuilder;
    }
}
