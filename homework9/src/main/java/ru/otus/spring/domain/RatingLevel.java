package ru.otus.spring.domain;

import lombok.Getter;

public enum RatingLevel {
    LOW("Низкий"),
    MIDDLE("Средний"),
    HIGH("Высокий");

    @Getter
    private final String description;

    RatingLevel(final String description) {
        this.description = description;
    }
}
