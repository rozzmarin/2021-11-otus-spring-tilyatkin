package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.BookReviewRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.BaseSpecification;
import ru.otus.spring.service.idGenerator.IdentifierGenerator;

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
    private final BaseSpecification<BookFilter> bookSpecification;
    private final BaseSpecification<BookReviewFilter> bookReviewSpecification;
    private final IdentifierGenerator<BookId> bookIdGenerator;
    private final IdentifierGenerator<AuthorId> authorIdGenerator;
    private final IdentifierGenerator<GenreId> genreIdGenerator;

    @Transactional(readOnly = true)
    @Override
    public Book find(BookId id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Book with id %s is not found", id)));
        prepareReviewsCount(List.of(book));
        return book;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> find(BookFilter filter) {
        List<Book> books = bookRepository.findAll(bookSpecification.toPredicate(filter));
        prepareReviewsCount(books);
        return books;
    }

    @Override
    public Book add(Book book) {
        prepareAuthors(book);
        prepareGenres(book);
        book.setBookId(bookIdGenerator.generate());
        return bookRepository.save(book);
    }

    @Override
    public Book edit(Book book) {
        prepareAuthors(book);
        prepareGenres(book);
        return bookRepository.save(book);
    }

    @Override
    public BookId remove(BookId id) {
        bookReviewRepository.deleteAllByBookBookId(id);
        bookRepository.deleteById(id);
        return id;
    }

    private void prepareAuthors(Book book) {
        Set<Author> authors = new HashSet<>();
        for (Author author : book.getAuthors()) {
            AuthorId authorId = author.getAuthorId();
            if (authorId == null) {
                author.setAuthorId(authorIdGenerator.generate());
                author = authorRepository.save(author);
            } else {
                author = authorRepository.findById(authorId).orElseThrow();
            }
            authors.add(author);
        }
        book.setAuthors(authors);
    }

    private void prepareGenres(Book book) {
        Set<Genre> genres = new HashSet<>();
        for (Genre genre : book.getGenres()) {
            GenreId genreId = genre.getGenreId();
            if (genreId == null) {
                genre.setGenreId(genreIdGenerator.generate());
                genre = genreRepository.save(genre);
            } else {
                genre = genreRepository.findById(genreId).orElseThrow();
            }
            genres.add(genre);
        }
        book.setGenres(genres);
    }

    private void prepareReviewsCount(List<Book> books) {
        Map<BookId, Long> reviewCounts = bookReviewRepository.countAtBooks(books.stream()
                .map(Book::getBookId)
                .collect(Collectors.toSet()));
        books.forEach(book -> {
            book.setReviewsCount(reviewCounts.getOrDefault(book.getBookId(), 0L));
        });
    }
}