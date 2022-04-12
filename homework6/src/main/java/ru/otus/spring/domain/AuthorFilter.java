package ru.otus.spring.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class AuthorFilter {
    private final Set<AuthorId> authorIds;
    private final String name;

    public boolean isAuthorIdsSpecified() {
        return authorIds != null && authorIds.size() > 0;
    }

    public boolean isNameSpecified() {
        return name != null && !name.isEmpty();
    }

    public boolean isSpecified() {
        return isAuthorIdsSpecified() || isNameSpecified();
    }
}
