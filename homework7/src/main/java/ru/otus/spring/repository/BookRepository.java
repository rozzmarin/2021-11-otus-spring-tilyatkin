package ru.otus.spring.repository;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookFilter;
import ru.otus.spring.domain.BookId;

import java.util.List;

public interface BookRepository {
    long count(BookFilter filter);

    Book get(BookId id);

    List<Book> get(BookFilter filter);

    BookId insert(Book book);

    BookId update(Book book);

    BookId delete(BookId id);
}
