package ru.otus.spring.domain;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthorFilter {
    private Set<AuthorId> authorIds;
    private String name;

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
