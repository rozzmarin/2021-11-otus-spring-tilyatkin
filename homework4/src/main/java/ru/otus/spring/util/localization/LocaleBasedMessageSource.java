package ru.otus.spring.util.localization;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocaleBasedMessageSource implements CustomMessageSource {
    private final MessageSource messageSource;
    private final LocaleProvider localeProvider;

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, code, localeProvider.getLocale());
    }
}
