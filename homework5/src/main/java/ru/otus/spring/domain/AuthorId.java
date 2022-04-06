package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class AuthorId {
    private final long authorId;
}
