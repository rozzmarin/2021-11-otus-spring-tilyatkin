package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public String readGenres(
            @ModelAttribute GenreFilter genreFilter,
            Model model
    ) {
        List<Genre> genres = genreService.find(genreFilter);
        model.addAttribute("genreFilter", genreFilter);
        model.addAttribute("genres", genres);
        return "genres/list";
    }

    @GetMapping("/genres/add")
    public String addGenre(
            Model model
    ) {
        model.addAttribute("genre", new GenreDto());
        return "genres/add";
    }

    @PostMapping("/genres/add")
    public String addGenre(
            @ModelAttribute("genre") @Valid GenreDto genre,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "genres/add";
        }
        genreService.add(genre.toDomain());
        return "redirect:/genres";
    }

    @GetMapping("/genres/{id}")
    public String editGenre(
            @PathVariable("id") GenreId id,
            Model model
    ) {
        GenreDto genre = GenreDto.fromDomain(genreService.find(id));
        model.addAttribute("genre", genre);
        return "genres/edit";
    }

    @PostMapping("/genres/{id}")
    public String editGenre(
            @PathVariable("id") GenreId id,
            @ModelAttribute("genre") @Valid GenreDto genre,
            BindingResult result,
            Model model
    ) {
        genre.setGenreId(id);
        if (result.hasErrors()) {
            return "genres/edit";
        }
        genreService.edit(genre.toDomain());
        return "redirect:/genres";
    }

    @PostMapping("/genres/{id}/remove")
    public String removeGenre(
            @PathVariable("id") GenreId id
    ) {
        genreService.remove(id);
        return "redirect:/genres";
    }
}