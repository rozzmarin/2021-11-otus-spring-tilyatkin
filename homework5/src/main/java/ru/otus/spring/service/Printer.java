package ru.otus.spring.service;

public interface Printer<T> {
    String print(T object);

    String printWithId(T object);
}
