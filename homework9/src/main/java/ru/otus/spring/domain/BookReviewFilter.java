package ru.otus.spring.domain;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BookReviewFilter {
    private Set<BookId> bookIds;
    private String reviewerName;
    private Set<RatingLevel> ratingLevel;

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