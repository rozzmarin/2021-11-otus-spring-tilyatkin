package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.*;
import ru.otus.spring.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {
    private BookId bookId;

    @NotNull(message = "Наименование не может быть пустым")
    @Size(min = 1, max = 255, message = "Наименование должно быть длиной от 1 до 255 символов")
    private String title;

    private String fullTitle;

    @NotEmpty(message = "Необходимо указать автора(ов)")
    private Set<AuthorId> authorIds;

    @NotEmpty(message = "Необходимо указать жанр(ы)")
    private Set<GenreId> genreIds;

    public Book toDomain() {
        return Book.builder()
                .bookId(bookId)
                .title(title)
                .authors(authorIds.stream()
                        .map(Author::new)
                        .collect(Collectors.toSet()))
                .genres(genreIds.stream()
                        .map(Genre::new)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static BookDto fromDomain(Book book) {
        return new BookDto(
                book.getBookId(),
                book.getTitle(),
                titleFromDomain(book),
                book.getAuthors().stream().map(Author::getAuthorId).collect(Collectors.toSet()),
                book.getGenres().stream().map(Genre::getGenreId).collect(Collectors.toSet()));
    }

    public static String titleFromDomain(Book book) {
        String authorLabel = book.getAuthors()
                .stream()
                .map(author -> String.format("%s %s", StringUtils.toShortName(author.getFirstname()), author.getLastname()))
                .collect(Collectors.joining(", "));
        return String.format("%s. %s", authorLabel, book.getTitle());
    }
}
