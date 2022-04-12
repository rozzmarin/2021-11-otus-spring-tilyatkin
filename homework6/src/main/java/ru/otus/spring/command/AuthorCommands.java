package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.printer.Printer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("authors")
@RequiredArgsConstructor
public class AuthorCommands {
    private final AuthorService authorService;
    private final Printer<Author> authorPrinter;

    @ShellMethod(value = "Get authors", key = {"author-get"})
    public String getAuthors(
            @ShellOption(help = "Author's id(s)", defaultValue = "") Set<AuthorId> authorIds,
            @ShellOption(help = "Author's name", defaultValue = "") String name
    ) {
        List<Author> authors = authorService.find(AuthorFilter.builder()
                .authorIds(authorIds)
                .name(name)
                .build());
        return authors
                .stream()
                .map(authorPrinter::fullPrint)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Add author", key = {"author-add"})
    public String addAuthor(
            @ShellOption(help = "Author's last name") String lastName,
            @ShellOption(help = "Author's first name") String firstName
    ) {
        Author newAuthor = authorService.add(Author.builder()
                .lastname(lastName)
                .firstname(firstName)
                .build());
        return newAuthor != null ?
                authorPrinter.fullPrint(newAuthor) :
                "Unable to add author";
    }

    @ShellMethod(value = "Edit author", key = {"author-edit"})
    public String editAuthor(
            @ShellOption(help = "Author's id") AuthorId authorId,
            @ShellOption(help = "Author's last name", defaultValue = "") String lastName,
            @ShellOption(help = "Author's first name", defaultValue = "") String firstName
    ) {
        Author author = authorService.find(authorId);
        if (!lastName.equals("")) {
            author.setLastname(lastName);
        }
        if (!firstName.equals("")) {
            author.setFirstname(firstName);
        }
        Author newAuthor = authorService.edit(author);
        return newAuthor != null ?
                authorPrinter.fullPrint(newAuthor) :
                "Unable to edit author";
    }

    @ShellMethod(value = "Remove author", key = {"author-remove"})
    public String removeAuthor(
            @ShellOption(help = "Author's id") AuthorId authorId
    ) {
        AuthorId oldAuthorId = authorService.remove(authorId);
        return oldAuthorId != null ?
                "Author has been removed" :
                "Unable to remove author";
    }
}
