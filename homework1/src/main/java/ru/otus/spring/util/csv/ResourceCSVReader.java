package ru.otus.spring.util.csv;

import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.List;

public class ResourceCSVReader implements CSVReader {
    private final String csvLocation;

    public ResourceCSVReader(String csvLocation) {
        this.csvLocation = csvLocation;
    }

    public List<String[]> readAll() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvLocation);
        if (inputStream == null) {
            throw new FileNotFoundException(csvLocation);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try (com.opencsv.CSVReader openCSVReader = new com.opencsv.CSVReader(inputStreamReader)) {
            return openCSVReader.readAll();
        }
        catch (CsvException ex) {
            throw new IOException(ex);
        }
    }
}
