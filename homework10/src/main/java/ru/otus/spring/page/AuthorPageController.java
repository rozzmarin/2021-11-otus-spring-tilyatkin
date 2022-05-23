package ru.otus.spring.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.domain.AuthorId;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {
    @GetMapping("/authors")
    public String readAuthors(Model model) {
        return "authors/list";
    }

    @GetMapping("/authors/add")
    public String addAuthor(Model model) {
        return "authors/edit";
    }

    @GetMapping("/authors/{id}")
    public String editAuthor(@PathVariable("id") AuthorId id, Model model) {
        model.addAttribute("authorId", id);
        return "authors/edit";
    }
}