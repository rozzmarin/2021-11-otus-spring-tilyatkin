package ru.otus.spring.util.localization;

public class LocaleProviderException extends RuntimeException {
    public LocaleProviderException(Throwable cause) {
        super(cause);
    }

    public LocaleProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
