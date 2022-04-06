package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookFilter;
import ru.otus.spring.domain.BookId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao dao;

    @Override
    public Book find(BookId id) {
        return dao.get(id);
    }

    @Override
    public List<Book> find(BookFilter filter) {
        return dao.get(filter);
    }

    @Override
    public Book add(Book book) {
        BookId bookId = dao.insert(book);
        if (bookId != null) {
            return dao.get(bookId);
        }
        return null;
    }

    @Override
    public Book edit(Book book) {
        BookId bookId = dao.update(book);
        if (bookId != null) {
            return dao.get(bookId);
        }
        return null;
    }

    @Override
    public BookId remove(BookId id) {
        return dao.delete(id);
    }
}