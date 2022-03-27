package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;

@Service
public class AuthorPrinter implements Printer<Author> {
    @Override
    public String print(Author object) {
        return String.format("%s, %s", object.getLastname(), object.getFirstname());
    }

    @Override
    public String printWithId(Author object) {
        return String.format("%d. %s, %s", object.getAuthorId().getAuthorId(), object.getLastname(), object.getFirstname());
    }
}
