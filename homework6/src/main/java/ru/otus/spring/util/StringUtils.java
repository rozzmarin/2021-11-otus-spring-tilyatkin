package ru.otus.spring.util;

public abstract class StringUtils {
    public static String quoted(String source, Character quote){
        if (source != null) {
            return quote + source + quote;
        }
        return null;
    }

    public static String toShortName(String source){
        if (source != null) {
            String result = "";
            String[] parts = source.split(" ");
            for (String part : parts) {
                result += part.charAt(0) + ".";
            }
            return result;
        }
        return null;
    }

    public static boolean isNullOrEmpty(String source) {
        return source == null || source.isEmpty();
    }
}
