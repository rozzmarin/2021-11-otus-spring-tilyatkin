package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class BookFilter {
    private final Set<BookId> bookIds;
    private final String title;
    private final AuthorFilter authorFilter;
    private final GenreFilter genreFilter;

    public boolean isBookIdsSpecified() {
        return bookIds != null && bookIds.size() > 0;
    }

    public boolean isTitleSpecified() {
        return title != null && !title.isEmpty();
    }

    public boolean isAuthorFilterSpecified() {
        return authorFilter != null;
    }

    public boolean isGenreFilterSpecified() {
        return genreFilter != null;
    }

    /*
    public boolean isAuthorIdSpecified() {
        return authorId != 0;
    }

    public boolean isAuthorNameSpecified() {
        return authorName != null && !authorName.isEmpty();
    }

    public boolean isGenreIdSpecified() {
        return genreId != 0;
    }

    public boolean isGenreTitleSpecified() {
        return genreTitle != null && !genreTitle.isEmpty();
    }
    */
}
