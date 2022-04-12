package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookReviewRepository bookReviewRepository;

    @Transactional(readOnly = true)
    @Override
    public Book find(BookId id) {
        Book book = bookRepository.get(id);
        Map<BookId, Long> reviewCounts = bookReviewRepository.countAtBooks(Set.of(id));
        book.setReviewsCount(reviewCounts.getOrDefault(id, 0L));
        return book;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> find(BookFilter filter) {
        List<Book> books = bookRepository.get(filter);
        Map<BookId, Long> reviewCounts = bookReviewRepository.countAtBooks(books.stream()
                .map(Book::getBookId)
                .collect(Collectors.toSet()));
        books.forEach(book -> book.setReviewsCount(reviewCounts.getOrDefault(book.getBookId(), 0L)));
        return books;
    }

    @Override
    public Book add(Book book) {
        prepareAuthors(book);
        prepareGenres(book);
        BookId bookId = bookRepository.insert(book);
        if (bookId != null) {
            return bookRepository.get(bookId);
        }
        return null;
    }

    @Override
    public Book edit(Book book) {
        prepareAuthors(book);
        prepareGenres(book);
        BookId bookId = bookRepository.update(book);
        if (bookId != null) {
            return bookRepository.get(bookId);
        }
        return null;
    }

    @Override
    public BookId remove(BookId id) {
        return bookRepository.delete(id);
    }

    private void prepareAuthors(Book book) {
        Set<Author> authors = new HashSet<>();
        for (Author author : book.getAuthors()) {
            AuthorId authorId = author.getAuthorId();
            if (authorId == null) {
                authorId = authorRepository.insert(author);
            }
            authors.add(authorRepository.get(authorId));
        }
        book.setAuthors(authors);
    }

    private void prepareGenres(Book book) {
        Set<Genre> genres = new HashSet<>();
        for (Genre genre : book.getGenres()) {
            GenreId genreId = genre.getGenreId();
            if (genreId == null) {
                genreId = genreRepository.insert(genre);
            }
            genres.add(genreRepository.get(genreId));
        }
        book.setGenres(genres);
    }
}