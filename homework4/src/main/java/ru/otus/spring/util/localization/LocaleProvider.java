package ru.otus.spring.util.localization;

import java.util.List;
import java.util.Locale;

public interface LocaleProvider {
    List<Locale> getLocales();
    Locale getLocale();
    void setLocale(Locale locale);
}
