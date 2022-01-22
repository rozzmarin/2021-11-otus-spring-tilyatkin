package ru.otus.spring.util.csv;

import java.util.List;
import java.util.Locale;

public interface CSVReader {
    List<List<String>> readAll(Locale locale);
}
