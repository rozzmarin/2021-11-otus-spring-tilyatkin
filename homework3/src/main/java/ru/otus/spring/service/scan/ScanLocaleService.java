package ru.otus.spring.service.scan;

import java.util.List;
import java.util.Locale;

public interface ScanLocaleService {
    Locale scan(List<Locale> availableLocales);
}
