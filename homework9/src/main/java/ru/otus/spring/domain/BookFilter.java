package ru.otus.spring.domain;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BookFilter {
    private Set<BookId> bookIds;
    private String title;
    private Long reviewsCount;
    private AuthorFilter authorFilter;
    private GenreFilter genreFilter;

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
