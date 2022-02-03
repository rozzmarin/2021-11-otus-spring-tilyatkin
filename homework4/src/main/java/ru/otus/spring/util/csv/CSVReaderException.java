package ru.otus.spring.util.csv;

public class CSVReaderException extends RuntimeException {
    public CSVReaderException(Throwable cause) {
        super(cause);
    }

    public CSVReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
