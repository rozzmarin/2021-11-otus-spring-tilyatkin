package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Genre;

@Service
public class GenrePrinter implements Printer<Genre> {
    @Override
    public String print(Genre object) {
        return String.format("%s", object.getTitle());
    }

    @Override
    public String printWithId(Genre object) {
        return String.format("%d. %s", object.getGenreId().getGenreId(), object.getTitle());
    }
}
