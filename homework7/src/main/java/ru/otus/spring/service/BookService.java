package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookFilter;
import ru.otus.spring.domain.BookId;

import java.util.List;

public interface BookService {
    Book find(BookId id);

    List<Book> find(BookFilter filter);

    Book add(Book book);

    Book edit(Book book);

    BookId remove(BookId id);
}
