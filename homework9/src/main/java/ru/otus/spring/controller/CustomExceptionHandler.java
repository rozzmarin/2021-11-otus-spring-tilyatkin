package ru.otus.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.spring.domain.exception.InvalidOperationException;
import ru.otus.spring.domain.exception.ObjectNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(ObjectNotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        modelAndView.addObject("errorMessage", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView badRequest(InvalidOperationException exception) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("errorMessage", exception.getMessage());
        return modelAndView;
    }
}
