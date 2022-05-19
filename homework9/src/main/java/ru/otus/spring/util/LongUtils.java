package ru.otus.spring.util;

public abstract class LongUtils {
    public static Long parseOrNull(String source){
        try {
            return Long.parseLong(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
