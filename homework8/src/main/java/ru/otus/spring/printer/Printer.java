package ru.otus.spring.printer;

public interface Printer<T> {
    String shortPrint(T object);

    String fullPrint(T object);
}
