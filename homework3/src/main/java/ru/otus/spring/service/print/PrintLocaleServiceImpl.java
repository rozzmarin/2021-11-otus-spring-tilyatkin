package ru.otus.spring.service.print;

import org.springframework.stereotype.Service;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.util.List;
import java.util.Locale;

@Service
public class PrintLocaleServiceImpl extends AbstractPrintService implements PrintLocaleService {
    private static final String undLocale = "und";

    public PrintLocaleServiceImpl(CustomMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public void print(List<Locale> locales) {
        if (locales != null) {
            for (int i = 0; i < locales.size(); i++) {
                Locale locale = locales.get(i);
                String displayName = undLocale.equals(locale.toLanguageTag()) ?
                        this.getMessage("quiz.locale.default-lang") :
                        locale.getDisplayName();
                this.getOut().println();
                this.getOut().printf("%d. %s", i + 1, displayName);
            }
        }
    }
}
