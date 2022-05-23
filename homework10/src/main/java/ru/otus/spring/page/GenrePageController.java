package ru.otus.spring.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.domain.GenreId;

@Controller
@RequiredArgsConstructor
public class GenrePageController {
    @GetMapping("/genres")
    public String readGenres(Model model) {
        return "genres/list";
    }

    @GetMapping("/genres/add")
    public String addGenre(Model model) {
        return "genres/edit";
    }

    @GetMapping("/genres/{id}")
    public String editGenre(@PathVariable("id") GenreId id, Model model) {
        model.addAttribute("genreId", id);
        return "genres/edit";
    }
}