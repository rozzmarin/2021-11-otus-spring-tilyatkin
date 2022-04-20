package ru.otus.spring.repository.specification;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.BookFilter;
import ru.otus.spring.domain.QBook;
import ru.otus.spring.domain.QBookReview;
import ru.otus.spring.domain.QGenre;
import ru.otus.spring.util.StringUtils;

@Component
public class BookSpecification implements BaseSpecification<BookFilter> {
    @Override
    public Predicate toPredicate(BookFilter filter) {
        BooleanBuilder expressionBuilder = new BooleanBuilder();
        if (filter.isBookIdsSpecified()) {
            expressionBuilder.and(QBook.book.bookId.in(filter.getBookIds()));
        }
        if (filter.isTitleSpecified()) {
            expressionBuilder.and(QBook.book.title.like(StringUtils.quoted(filter.getTitle(), '%')));
        }
        if (filter.isAuthorFilterSpecified()) {
            if (filter.getAuthorFilter().isAuthorIdsSpecified()) {
                expressionBuilder.and(QBook.book.authors.any().authorId.in(filter.getAuthorFilter().getAuthorIds()));
            }
            if (filter.getAuthorFilter().isNameSpecified()) {
                String pattern = StringUtils.quoted(filter.getAuthorFilter().getName(), '%');
                expressionBuilder.andAnyOf(
                        QBook.book.authors.any().firstname.like(pattern),
                        QBook.book.authors.any().lastname.like(pattern));
            }
        }
        if (filter.isGenreFilterSpecified()) {
            if (filter.getGenreFilter().isGenreIdsSpecified()) {
                expressionBuilder.and(QBook.book.genres.any().genreId.in(filter.getGenreFilter().getGenreIds()));
            }
            if (filter.getGenreFilter().isTitleSpecified()) {
                expressionBuilder.and(QBook.book.genres.any().title.like(StringUtils.quoted(filter.getGenreFilter().getTitle(), '%')));
            }
        }
        return expressionBuilder;
    }
}
