package ru.otus.spring.domain;

import java.util.Objects;

public class Answer {
    private final String body;

    public Answer(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(body, answer.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }
}
