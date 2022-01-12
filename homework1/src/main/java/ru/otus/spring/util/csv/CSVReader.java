package ru.otus.spring.util.csv;

import java.io.IOException;
import java.util.List;

public interface CSVReader {
    List<String[]> readAll() throws IOException;
}
