package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.util.csv.CSVReader;
import ru.otus.spring.util.localization.LocaleProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractDaoCSV {
    private final CSVReader csvReader;
    private final LocaleProvider localeProvider;
    private final Map<Locale, List<List<String>>> rowListMap = new HashMap<>();

    protected final List<List<String>> getRowList() {
        List<List<String>> rowList = this.rowListMap.computeIfAbsent(localeProvider.getLocale(),
                locale -> csvReader.readAll(locale));
        return rowList;
    }
}