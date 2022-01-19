package ru.otus.spring.util.csv;

import java.util.List;

public interface CSVReader {
    List<List<String>> readAll();
}
