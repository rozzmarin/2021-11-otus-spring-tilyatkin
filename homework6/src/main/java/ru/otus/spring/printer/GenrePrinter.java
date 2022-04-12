package ru.otus.spring.printer;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Genre;

@Service
public class GenrePrinter implements Printer<Genre> {
    @Override
    public String shortPrint(Genre object) {
        return String.format("%s", object.getTitle());
    }

    @Override
    public String fullPrint(Genre object) {
        return String.format("%d. %s", object.getGenreId().getGenreId(), object.getTitle());
    }
}
