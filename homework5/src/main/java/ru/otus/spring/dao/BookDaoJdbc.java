package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.stereotype.Repository;
import ru.otus.spring.dao.exception.ObjectNotFoundException;
import ru.otus.spring.dao.jdbc.context.CommonContext;
import ru.otus.spring.domain.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final CommonContext context;

    @Override
    public long count(BookFilter filter) {
        return context.getBooksCount(filter);
    }

    @Override
    public Book get(BookId id) {
        BookFilter filter = BookFilter.builder()
                .bookIds(Set.of(id))
                .build();
        List<Book> books = get(filter);
        if (books.size() == 0) {
            throw new ObjectNotFoundException(String.format("Book with id %s is not found", id.getBookId()));
        }

        return books.get(0);
    }

    @Override
    public List<Book> get(BookFilter filter) {
        List<Book> books = context.getBooks(filter);
        if (books.size() > 0) {
            List<BookId> bookIds = books.stream()
                    .map(Book::getBookId)
                    .collect(Collectors.toList());
            List<Pair<BookId, Author>> bookAuthors = context.getBookAuthors(bookIds);
            List<Pair<BookId, Genre>> bookGenres = context.getBookGenres(bookIds);
            books = books.stream()
                    .map(book -> new Book(
                            book.getBookId(),
                            book.getTitle(),
                            bookAuthors.stream()
                                    .filter(ba -> ba.getValue0().equals(book.getBookId()))
                                    .map(Pair::getValue1)
                                    .collect(Collectors.toSet()),
                            bookGenres.stream()
                                    .filter(ba -> ba.getValue0().equals(book.getBookId()))
                                    .map(Pair::getValue1)
                                    .collect(Collectors.toSet())))
                    .collect(Collectors.toList());
        }
        return books;
    }

    @Override
    public BookId insert(Book book) {
        BookId bookId = context.insertBook(book);
        for (Author author : book.getAuthors()) {
            AuthorId authorId = author.getAuthorId();
            if (authorId == null) {
                authorId = context.insertAuthor(author);
            }
            context.insertBookAuthor(Pair.with(bookId, authorId));
        }
        for (Genre genre : book.getGenres()) {
            GenreId genreId = genre.getGenreId();
            if (genreId == null) {
                genreId = context.insertGenre(genre);
            }
            context.insertBookGenre(Pair.with(bookId, genreId));
        }
        return bookId;
    }

    @Override
    public BookId update(Book book) {
        BookId bookId = context.updateBook(book);
        if (bookId != null) {
            context.deleteBookAuthors(bookId);
            for (Author author : book.getAuthors()) {
                AuthorId authorId = author.getAuthorId();
                if (authorId == null) {
                    authorId = context.insertAuthor(author);
                }
                context.insertBookAuthor(Pair.with(bookId, authorId));
            }
            context.deleteBookGenres(bookId);
            for (Genre genre : book.getGenres()) {
                GenreId genreId = genre.getGenreId();
                if (genreId == null) {
                    genreId = context.insertGenre(genre);
                }
                context.insertBookGenre(Pair.with(bookId, genreId));
            }
        }
        return bookId;
    }

    @Override
    public BookId delete(BookId bookId) {
        context.deleteBookAuthors(bookId);
        context.deleteBookGenres(bookId);
        return context.deleteBook(bookId);
    }
}
