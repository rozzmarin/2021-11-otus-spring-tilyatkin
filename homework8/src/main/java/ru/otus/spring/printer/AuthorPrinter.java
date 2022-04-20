package ru.otus.spring.printer;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.util.StringUtils;

@Service
public class AuthorPrinter implements Printer<Author> {
    @Override
    public String shortPrint(Author object) {
        return String.format("%s %s", StringUtils.toShortName(object.getFirstname()), object.getLastname());
    }

    @Override
    public String fullPrint(Author object) {
        return String.format("%s. %s, %s", object.getAuthorId(), object.getLastname(), object.getFirstname());
    }
}
