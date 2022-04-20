package ru.otus.spring.repository.specification;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.QAuthor;
import ru.otus.spring.util.StringUtils;

@Component
public class AuthorSpecification implements BaseSpecification<AuthorFilter> {
    @Override
    public Predicate toPredicate(AuthorFilter filter) {
        BooleanBuilder expressionBuilder = new BooleanBuilder();
        if (filter.isAuthorIdsSpecified()) {
            expressionBuilder.and(QAuthor.author.authorId.in(filter.getAuthorIds()));
        }
        if (filter.isNameSpecified()) {
            String pattern = StringUtils.quoted(filter.getName(), '%');
            expressionBuilder.andAnyOf(
                    QAuthor.author.firstname.like(pattern),
                    QAuthor.author.lastname.like(pattern));
        }
        return expressionBuilder;
    }
}
