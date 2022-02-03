package ru.otus.spring.service.print;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.io.PrintStream;

@RequiredArgsConstructor
public abstract  class AbstractPrintService {
    private final CustomMessageSource messageSource;

    protected String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args);
    }

    protected PrintStream getOut() {
        return System.out;
    }
}
