package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class BookFilter {
    private final Set<BookId> bookIds;
    private final String title;
    private final Long reviewsCount;
    private final AuthorFilter authorFilter;
    private final GenreFilter genreFilter;

    public boolean isBookIdsSpecified() {
        return bookIds != null && bookIds.size() > 0;
    }

    public boolean isTitleSpecified() {
        return title != null && !title.isEmpty();
    }

    public boolean isReviewsCountSpecified() {
        return reviewsCount != null;
    }

    public boolean isAuthorFilterSpecified() {
        return authorFilter != null && authorFilter.isSpecified();
    }

    public boolean isGenreFilterSpecified() {
        return genreFilter != null && genreFilter.isSpecified();
    }
}
