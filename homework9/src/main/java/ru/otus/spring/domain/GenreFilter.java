package ru.otus.spring.domain;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GenreFilter {
    private Set<GenreId> genreIds;
    private String title;

    public boolean isGenreIdsSpecified() {
        return genreIds != null && genreIds.size() > 0;
    }

    public boolean isTitleSpecified() {
        return title != null && !title.isEmpty();
    }

    public boolean isSpecified() {
        return isGenreIdsSpecified() || isTitleSpecified();
    }
}
