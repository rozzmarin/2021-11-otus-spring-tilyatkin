package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class BookReviewFilter {
    private final Set<BookId> bookIds;
    private final String reviewerName;
    private final Set<RatingLevel> ratingLevel;

    public boolean isBookIdsSpecified() {
        return bookIds != null && bookIds.size() > 0;
    }

    public boolean isReviewerNameSpecified() {
        return reviewerName != null && !reviewerName.isEmpty();
    }

    public boolean isRatingLevelSpecified() {
        return ratingLevel != null && ratingLevel.size() > 0;
    }
}