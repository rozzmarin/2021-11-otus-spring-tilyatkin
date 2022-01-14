package ru.otus.spring.dao;

import ru.otus.spring.util.csv.CSVReader;

import java.util.List;

public abstract class AbstractDaoCSV {
    private final CSVReader csvReader;
    private List<List<String>> rowList;

    public AbstractDaoCSV(CSVReader csvReader) {
        this.csvReader = csvReader;
    }

    protected final List<List<String>> getRowList() {
        if (rowList == null) {
            rowList = csvReader.readAll();
        }
        return rowList;
    }
}