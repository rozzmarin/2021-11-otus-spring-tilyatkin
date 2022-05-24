package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.RatingLevel;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RatingLevelDto {
    public String value;
    public String description;

    public static RatingLevelDto fromDomain(RatingLevel ratingLevel) {
        return new RatingLevelDto(ratingLevel.name(), ratingLevel.getDescription());
    }
}