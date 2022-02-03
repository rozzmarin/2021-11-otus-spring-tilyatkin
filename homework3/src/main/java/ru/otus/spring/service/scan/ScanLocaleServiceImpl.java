package ru.otus.spring.service.scan;

import org.springframework.stereotype.Service;
import ru.otus.spring.util.localization.LocaleProvider;
import ru.otus.spring.util.localization.CustomMessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Service
public class ScanLocaleServiceImpl extends AbstractScanService implements ScanLocaleService {
    public ScanLocaleServiceImpl(CustomMessageSource messageSource, LocaleProvider localeResolver) {
        super(messageSource);
    }

    @Override
    public Locale scan(List<Locale> availableLocales) {
        Locale locale = availableLocales != null && availableLocales.size() > 0 ?
                getAvailableLocale(availableLocales) :
                getLocale();
        return locale;
    }

    private Locale getAvailableLocale(List<Locale> availableLocales) {
        Scanner scanner = new Scanner(this.getIn());
        int inputInt;
        do {
            this.getOut().println();
            this.getOut().printf("%s: ", this.getMessage("quiz.locale.label"));
            String inputStr = scanner.nextLine();
            try {
                inputInt = Integer.parseInt(inputStr);
            }
            catch (NumberFormatException ex) {
                inputInt = 0;
            }
        }
        while (inputInt <= 0 || inputInt > availableLocales.size());
        Locale locale = availableLocales.get(inputInt - 1);
        return locale;
    }

    private Locale getLocale() {
        return Locale.getDefault();
    }
}
