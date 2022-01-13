package ru.otus.spring.util.csv;

import java.io.*;
import java.util.*;

public class ResourceCSVReader implements CSVReader {
    private final String csvLocation;

    public ResourceCSVReader(String csvLocation) {
        this.csvLocation = csvLocation;
    }

    @Override
    public List<List<String>> readAll() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvLocation);
        if (inputStream == null) {
            throw new MissingResourceException("Can't find specified csv", "", csvLocation);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try (com.opencsv.CSVReader openCSVReader = new com.opencsv.CSVReader(inputStreamReader)) {
            List result = new ArrayList<List<String>>();
            List<String[]> allRows = openCSVReader.readAll();
            for (String[] row : allRows) {
                result.add(Arrays.asList(row));
            }
            return result;
        }
        catch (Exception ex) {
            throw new CSVReaderException("Problem with reading csv", ex);
        }
    }
}
