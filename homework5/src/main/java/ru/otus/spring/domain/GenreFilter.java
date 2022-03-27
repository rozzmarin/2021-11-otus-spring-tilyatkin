package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class GenreFilter {
    private final Set<GenreId> genreIds;
    private final String title;

    public boolean isGenreIdsSpecified() {
        return genreIds != null && genreIds.size() > 0;
    }

    public boolean isTitleSpecified() {
        return title != null && !title.isEmpty();
    }
}
