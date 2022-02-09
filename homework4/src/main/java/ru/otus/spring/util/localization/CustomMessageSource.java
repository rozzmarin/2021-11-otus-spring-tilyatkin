package ru.otus.spring.util.localization;

public interface CustomMessageSource {
    String getMessage(String code, Object... args);
}
