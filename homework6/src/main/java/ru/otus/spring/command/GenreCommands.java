package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.printer.Printer;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("genres")
@RequiredArgsConstructor
public class GenreCommands {
    private final GenreService genreService;
    private final Printer<Genre> genrePrinter;

    @ShellMethod(value = "Get genres", key = {"genre-get"})
    public String getGenres(
            @ShellOption(help = "Genre's title", defaultValue = "") String title
    ) {
        List<Genre> genres = genreService.find(GenreFilter.builder()
                .title(title)
                .build());
        return genres
                .stream()
                .map(genrePrinter::fullPrint)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Add genre", key = {"genre-add"})
    public String addGenre(
            @ShellOption(help = "Genre's title") String title
    ) {
        Genre newGenre = genreService.add(Genre.builder()
                .title(title)
                .build());
        return newGenre != null ?
                genrePrinter.fullPrint(newGenre) :
                "Unable to add genre";
    }

    @ShellMethod(value = "Edit genre", key = {"genre-edit"})
    public String editGenre(
            @ShellOption(help = "Genre's id") GenreId genreId,
            @ShellOption(help = "Genre's title") String title
    ) {
        Genre genre = genreService.find(genreId);
        genre.setTitle(title);
        Genre newGenre = genreService.edit(genre);
        return newGenre != null ?
                genrePrinter.fullPrint(newGenre) :
                "Unable to edit genre";
    }

    @ShellMethod(value = "Remove genre", key = {"genre-remove"})
    public String removeGenre(
            @ShellOption(help = "Genre's id") GenreId genreId
    ) {
        GenreId oldGenreId = genreService.remove(genreId);
        return oldGenreId != null ?
                "Genre has been removed" :
                "Unable to remove genre";
    }
}