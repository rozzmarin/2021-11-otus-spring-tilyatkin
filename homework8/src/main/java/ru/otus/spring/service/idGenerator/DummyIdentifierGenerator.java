package ru.otus.spring.service.idGenerator;

import java.math.BigInteger;
import java.util.Random;

public abstract class DummyIdentifierGenerator {
    private static final Random random = new Random();

    public static String generateTemplateString(String prefix, int length) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        int numberLength = length - prefix.length();
        if (numberLength < 1 || numberLength > 10) {
            throw new IllegalArgumentException();
        }
        int number = random.nextInt((int)Math.pow(10, numberLength) - 1) + 1;
        return String.format("%s%" + numberLength + "s", prefix, number).replace(' ', '0');
    }
}
